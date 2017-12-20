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

package org.apache.fineract.infrastructure.paymentchannel.service;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.paymentchannel.domain.PaymentChannelRepository;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentChannelWritePlatformServiceImpl implements PaymentChannelWritePlatformService{
	
	private final PlatformSecurityContext securityContext;
	private final PaymentChannelRepository  paymentChannelRepository;
	
	@Autowired
	public PaymentChannelWritePlatformServiceImpl(PlatformSecurityContext securityContext,
			PaymentChannelRepository  paymentChannelRepository) {
		super();
		this.securityContext = securityContext;
		this.paymentChannelRepository = paymentChannelRepository;
	}

	@Override
	public CommandProcessingResult createPaymentChannel(JsonCommand command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandProcessingResult updatePaymentChannel(Long chargeId, JsonCommand command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandProcessingResult deletePaymentChannel(Long chargeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
