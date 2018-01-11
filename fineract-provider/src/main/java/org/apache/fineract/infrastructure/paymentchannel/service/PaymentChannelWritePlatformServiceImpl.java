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

import java.util.Map;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.paymentchannel.domain.PaymentChannel;
import org.apache.fineract.infrastructure.paymentchannel.domain.PaymentChannelRepository;
import org.apache.fineract.infrastructure.sms.service.SmsWritePlatformServiceJpaRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class PaymentChannelWritePlatformServiceImpl implements PaymentChannelWritePlatformService {

	private final static Logger logger = LoggerFactory.getLogger(SmsWritePlatformServiceJpaRepositoryImpl.class);
	private final PaymentChannelRepository paymentChannelRepository;

	@Autowired
	public PaymentChannelWritePlatformServiceImpl(PaymentChannelRepository paymentChannelRepository) {
		super();
		this.paymentChannelRepository = paymentChannelRepository;
	}

	@Override
	public CommandProcessingResult create(JsonCommand command) {
		try {
			PaymentChannel paymentChannel = PaymentChannel.fromJson(command);
			paymentChannel = paymentChannelRepository.save(paymentChannel);
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

			final PaymentChannel paymentChannel = paymentChannelRepository.findOne(resourceId);
			final Map<String, Object> changes = paymentChannel.update(command);
			if (!changes.isEmpty()) {
				this.paymentChannelRepository.save(paymentChannel);
			}

			return new CommandProcessingResultBuilder() //
					.withCommandId(command.commandId()) //
					.withEntityId(resourceId) //
					.with(changes) //
					.build();
		} catch (final DataIntegrityViolationException dve) {
			handleDataIntegrityIssues(command, dve);
			return CommandProcessingResult.empty();
		}
	}

	@Override
	public CommandProcessingResult delete(Long resourceId) {
		try {
			final PaymentChannel paymentChannel = paymentChannelRepository.findOne(resourceId);
			this.paymentChannelRepository.delete(paymentChannel);
			this.paymentChannelRepository.flush();
		} catch (final DataIntegrityViolationException dve) {
			handleDataIntegrityIssues(null, dve);
			return CommandProcessingResult.empty();
		}
		return new CommandProcessingResultBuilder().withEntityId(resourceId).build();
	}

	private void handleDataIntegrityIssues(@SuppressWarnings("unused") final JsonCommand command,
			final DataIntegrityViolationException dve) {
		final Throwable realCause = dve.getMostSpecificCause();

		logger.error(dve.getMessage(), dve);
	}
}
