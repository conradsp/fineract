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

import java.util.Map;
import java.util.List;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.domain.PaymentGateway;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.domain.PaymentGatewayRepository;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.paymenttype.domain.PaymentType;
import org.apache.fineract.portfolio.paymenttype.domain.PaymentTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayWritePlatformServiceImpl implements PaymentGatewayWritePlatformService {

	private final static Logger logger = LoggerFactory.getLogger(PaymentGatewayWritePlatformServiceImpl.class);
	private final PaymentGatewayRepository paymentGatewayRepository;
	private final PlatformSecurityContext context;
	private final PaymentTypeRepository paymentTypeRepository;

	@Autowired
	public PaymentGatewayWritePlatformServiceImpl(PaymentGatewayRepository paymentGatewayRepository,
												  PlatformSecurityContext context, PaymentTypeRepository paymentTypeRepository) {
		super();
		this.paymentGatewayRepository = paymentGatewayRepository;
		this.context = context;
		this.paymentTypeRepository = paymentTypeRepository;
	}

	@Override
	public CommandProcessingResult create(JsonCommand command) {
		try {
			PaymentGateway paymentChannel = PaymentGateway.fromJson(command);
			
			final Long paymentTypeId = command.longValueOfParameterNamed("paymentTypeId");
			
			paymentChannel.setPaymentType(paymentTypeId);
			paymentChannel.setCreatedBy(this.context.authenticatedUser().getId());
			paymentChannel = paymentGatewayRepository.save(paymentChannel);
			return new CommandProcessingResultBuilder() //
					.withCommandId(command.commandId()) //
					.withEntityId(paymentChannel.getId()) //
					.build();
		} catch (final DataIntegrityViolationException dve) {
			handleDataIntegrityIssues(command, dve);
			return CommandProcessingResult.empty();
		}
	}

	@Override
	public CommandProcessingResult update(Long resourceId, JsonCommand command) {
		try {
			Map<String, Object> changes = null;
			final List<PaymentGateway> paymentGateway = paymentGatewayRepository.findAll();
			if (paymentGateway.isEmpty()) {
				PaymentGateway newGatewayConfig = new PaymentGateway();
				changes = newGatewayConfig.update(command);
				if (!changes.isEmpty()) {
					this.paymentGatewayRepository.save(newGatewayConfig);
				}
			} else {
				changes = paymentGateway.get(0).update(command);
				if (!changes.isEmpty()) {
					this.paymentGatewayRepository.save(paymentGateway.get(0));
				}
			}

			if (!changes.isEmpty()) {
				this.paymentGatewayRepository.save(paymentGateway);
			}

			return new CommandProcessingResultBuilder() //
					.withCommandId(command.commandId()) //
					.withEntityId((long) 1) //
					.with(changes) //
					.build();
		} catch (final DataIntegrityViolationException dve) {
			handleDataIntegrityIssues(command, dve);
			return CommandProcessingResult.empty();
		}
	}

	private void handleDataIntegrityIssues(@SuppressWarnings("unused") final JsonCommand command,
			final DataIntegrityViolationException dve) {
		final Throwable realCause = dve.getMostSpecificCause();

		logger.error(dve.getMessage(), dve);
	}
}
