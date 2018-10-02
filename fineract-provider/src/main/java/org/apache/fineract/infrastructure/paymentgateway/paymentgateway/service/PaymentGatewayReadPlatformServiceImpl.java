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

package org.apache.fineract.infrastructure.paymentgateway.paymentgateway.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.data.PaymentGatewayData;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.domain.PaymentGateway;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.domain.PaymentGatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayReadPlatformServiceImpl implements PaymentGatewayReadPlatformService {
	
	private final PaymentGatewayRepository paymentGatewayRepository;
	private final RoutingDataSource dataSource;
	
	@Autowired
	public PaymentGatewayReadPlatformServiceImpl(final RoutingDataSource dataSource,
												 PaymentGatewayRepository paymentGatewayRepository) {
		this.dataSource = dataSource;
		this.paymentGatewayRepository = paymentGatewayRepository;
	}

	@Override
	public PaymentGatewayData retrievePaymentGatewayData() {
		PaymentGatewayData paymentGateway = null;
		List<PaymentGateway> gatewayDefs = paymentGatewayRepository.findAll();

		if (!gatewayDefs.isEmpty()) {
			paymentGateway = new PaymentGatewayData(gatewayDefs.get(0));
		}

		return paymentGateway;
	}

	@Override
	public boolean isPaymentGatewayActive() {
		boolean bActive = false;
		PaymentGatewayData paymentGateway = null;
		List<PaymentGateway> gatewayDefs = paymentGatewayRepository.findAll();

		if (!gatewayDefs.isEmpty()) {
			paymentGateway = new PaymentGatewayData(gatewayDefs.get(0));
			bActive = paymentGateway.isActive();
		}

		return bActive;
	}

}
