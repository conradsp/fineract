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

import org.apache.fineract.infrastructure.paymentgateway.gateway.service.GatewayEventListener;
import org.apache.fineract.infrastructure.paymentgateway.gateway.util.PaymentGatewayConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

@Service
public class OutboundChannelHelper implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	private GatewayMessagingConfig messagingConfig;

	@Autowired
	public OutboundChannelHelper(@Lazy GatewayMessagingConfig messagingConfig) {
		this.messagingConfig = messagingConfig;
	}

	public void sendMessage(String channelUsage, String message) {
		String channelName = PaymentGatewayConstants.ACTIVEMQ_SUBSCRIBER_SERVICE_NAME;
		MessageProducer producer = messagingConfig.getMessageProducer(channelName+"."+channelUsage);
		Session session = messagingConfig.getOutboundSession();

		//TextMessage payload = TextMessage.withPayload(message).setHeader(PaymentGatewayConstants.CHANNEL_NAME_HEADER, channelName)
		//		.setHeader(PaymentGatewayConstants.CHANNEL_USAGE_HEADER, channelUsage).build();

		// Send the message.
		try {
			producer.send(session.createTextMessage(message));
		} catch (JMSException E) {

		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
