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

import javax.annotation.PostConstruct;

import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.service.PaymentGatewayReadPlatformService;
import org.apache.fineract.infrastructure.security.service.TenantDetailsService;
import org.apache.fineract.infrastructure.paymentgateway.gateway.util.PaymentGatewayConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitGatewayQueues {
	private TenantDetailsService tenantDetailsService;

	@Autowired
	private GatewayMessagingConfig messagingConfig;
	@Autowired
	private PaymentGatewayReadPlatformService paymentGatewayReadPlatformService;
	@Autowired
	private GatewayMessagingConfig gatewayMessagingConfig;

	@Autowired
	public void setTenantDetailsService(TenantDetailsService tenantDetailsService) {
		this.tenantDetailsService = tenantDetailsService;
	}

	@PostConstruct
	public void init() {
		// We are going to initialize a single request queue and response queue, not one per payment channel
        gatewayMessagingConfig.setChannelName(PaymentGatewayConstants.ACTIVEMQ_SUBSCRIBER_SERVICE_NAME);
        gatewayMessagingConfig.connectQueue();
	}
}