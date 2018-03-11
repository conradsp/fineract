/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.fineract.infrastructure.paymentgateway.gateway.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.fineract.infrastructure.paymentgateway.gateway.exception.ChannelNotFoundException;
import org.apache.fineract.infrastructure.paymentgateway.gateway.util.PaymentGatewayConstants;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.data.PaymentChannelData;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannel;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.messaging.MessageChannel;

public class DynamicAMQChannelResolver {

	// In production environment this value will be significantly higher
	// This is just to demonstrate the concept of limiting the max number of
	// Dynamically created application contexts we'll hold in memory when we execute
	// the code from a junit
	public static final int MAX_CACHE_SIZE = 10;

	private final LinkedHashMap<String, Map<String, MessageChannel>> channelSets = new LinkedHashMap<String, Map<String, MessageChannel>>() {

		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Entry<String, Map<String, MessageChannel>> eldest) {
			// This returning true means the least recently used
			// channel and its application context will be closed and removed
			boolean remove = size() > MAX_CACHE_SIZE;
			if (remove) {
				String channelName = eldest.getKey();
				ConfigurableApplicationContext ctx = contexts.get(channelName);
				if (ctx != null) { // shouldn't be null ideally
					ctx.close();
					contexts.remove(channelName);
				}
			}
			return remove;
		}

	};

	private final Map<String, ConfigurableApplicationContext> contexts = new HashMap<String, ConfigurableApplicationContext>();

	/**
	 * Resolve a payment channel, where each payment channel gets a private
	 * application context and the channel is the inbound channel to that
	 * application context.
	 *
	 * @param name
	 * @param messageType
	 * @return a channel
	 */
	public MessageChannel resolve(String name, String usageType) throws ChannelNotFoundException {
		Map<String, MessageChannel> channelSet = this.channelSets.get(name);
		if (channelSet == null)
			throw new ChannelNotFoundException("Payment channel " + name + " not exception");
		return channelSet.get(usageType);
	}

	public synchronized Map<String, MessageChannel> createPaymentChannel(PaymentChannelData paymentChannelData) {
		String channelName = paymentChannelData.getChannelName();
		Map<String, MessageChannel> channelSet = this.channelSets.get(channelName);
		if (channelSet == null) {
			ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
					new String[] { "META-INF/spring/dynamic-amq-outbound-adapter-template.xml" }, false);
			this.setEnvironmentForCustomer(ctx, paymentChannelData.getRequestQueue(),
					paymentChannelData.getResponseQueue(), paymentChannelData.getChannelBrokerEndpoint());
			ctx.refresh();
			channelSet = new HashMap<>();
			// Create request channel
			MessageChannel requestChannel = ctx.getBean("toAMQRequestChannel", MessageChannel.class);
			channelSet.put(PaymentGatewayConstants.CHANNEL_REQUEST_USAGE, requestChannel);

			MessageChannel responseChannel = ctx.getBean("toAMQResponseChannel", MessageChannel.class);
			channelSet.put(PaymentGatewayConstants.CHANNEL_RESPONSE_USAGE, responseChannel);
			this.channelSets.put(channelName, channelSet);
			// Will works as the same reference is presented always
			this.contexts.put(channelName, ctx);
		}
		return channelSet;
	}

	public synchronized void removePaymentChannel(String channelName) {
		Map<String, MessageChannel> channelSet = channelSets.get(channelName);

		if (channelSet != null) {
			ConfigurableApplicationContext ctx = contexts.get(channelName);
			if (ctx != null) {
				ctx.close();
				contexts.remove(channelName);
			}
		}
	}

	/**
	 * Set environment support to set properties for the channel-specific
	 * application context.
	 *
	 * @param ctx
	 * @param requestQueue
	 * @param responseQueue
	 * @param brokerURL
	 */
	private void setEnvironmentForCustomer(ConfigurableApplicationContext ctx, String requestQueue,
			String responseQueue, String brokerURL) {
		StandardEnvironment env = new StandardEnvironment();
		Properties props = new Properties();
		// populate properties for customer
		props.setProperty("channel.brokerURL", brokerURL);
		props.setProperty("channel.outboundRequestQueue", requestQueue);
		props.setProperty("channel.outboundResponseQueue", responseQueue);

		PropertiesPropertySource pps = new PropertiesPropertySource("amqprops", props);
		env.getPropertySources().addLast(pps);
		ctx.setEnvironment(env);
	}

	public LinkedHashMap<String, Map<String, MessageChannel>> getChannels() {
		return channelSets;
	}
}
