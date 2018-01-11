package org.apache.fineract.infrastructure.paymentgateway.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.fineract.infrastructure.paymentgateway.exception.ChannelNotFoundException;
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

	private final LinkedHashMap<String, MessageChannel> channels = new LinkedHashMap<String, MessageChannel>() {

		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Entry<String, MessageChannel> eldest) {
			// This returning true means the least recently used
			// channel and its application context will be closed and removed
			boolean remove = size() > MAX_CACHE_SIZE;
			if (remove) {
				MessageChannel channel = eldest.getValue();
				ConfigurableApplicationContext ctx = contexts.get(channel);
				if (ctx != null) { // shouldn't be null ideally
					ctx.close();
					contexts.remove(channel);
				}
			}
			return remove;
		}

	};

	private final Map<MessageChannel, ConfigurableApplicationContext> contexts = new HashMap<MessageChannel, ConfigurableApplicationContext>();

	/**
	 * Resolve a payment channel, where each payment channel gets a private
	 * application context and the channel is the inbound channel to that
	 * application context.
	 *
	 * @param name
	 * @return a channel
	 */
	public MessageChannel resolve(String name) throws ChannelNotFoundException {
		MessageChannel channel = this.channels.get(name);
		if (channel == null)
			throw new ChannelNotFoundException("Payment channel " + name + " not exception");
		return channel;
	}

	public synchronized MessageChannel createPaymentChannel(String name, String brokerURL) {
		MessageChannel channel = this.channels.get(name);
		if (channel == null) {
			ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
					new String[]{"dynamic-amq-outbound-adapter-template.xml"}, false);
			this.setEnvironmentForCustomer(ctx, name, brokerURL);
			ctx.refresh();
			channel = ctx.getBean("toAMQChannel", MessageChannel.class);
			this.channels.put(name, channel);
			// Will works as the same reference is presented always
			this.contexts.put(channel, ctx);
		}
		return channel;
	}

    public synchronized void removePaymentChannel(String name) {
		MessageChannel channel = channels.get(name);

		if (channel != null) {
			ConfigurableApplicationContext ctx = contexts.get(channel);
			if (ctx != null) {
				ctx.close();
				contexts.remove(channel);
			}
		}
	}

	/**
	 * Set environment support to set properties for the channel-specific
	 * application context.
	 *
	 * @param ctx
	 * @param brokerURL
	 */
	private void setEnvironmentForCustomer(ConfigurableApplicationContext ctx,
			String name, String brokerURL) {
		StandardEnvironment env = new StandardEnvironment();
		Properties props = new Properties();
		// populate properties for customer
		props.setProperty("channel.brokerURL", brokerURL);
		props.setProperty("channel.outboundQueueName", name);

		PropertiesPropertySource pps = new PropertiesPropertySource("amqprops", props);
		env.getPropertySources().addLast(pps);
		ctx.setEnvironment(env);
	}

	public LinkedHashMap<String, MessageChannel> getChannels() {
		return channels;
	}
}
