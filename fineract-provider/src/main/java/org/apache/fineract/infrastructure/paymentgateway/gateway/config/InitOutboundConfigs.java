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

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.data.PaymentChannelData;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.service.PaymentChannelReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class InitOutboundConfigs {
	@Autowired
	@Qualifier("paymentChannelResolver")
	private DynamicAMQChannelResolver amqChannelResolver;
	@Autowired
	private PaymentChannelReadPlatformService paymentChannelReadPlatformService;

	@PostConstruct
	public void init() {
		Collection<PaymentChannelData> paymentChannelList = paymentChannelReadPlatformService
				.retrieveAllPaymentChannelData();
		for (PaymentChannelData paymentChannelData : paymentChannelList) {
			amqChannelResolver.createPaymentChannel(paymentChannelData.getChannelName(),
					paymentChannelData.getChannelBrokerEndpoint());
		}
	}
}