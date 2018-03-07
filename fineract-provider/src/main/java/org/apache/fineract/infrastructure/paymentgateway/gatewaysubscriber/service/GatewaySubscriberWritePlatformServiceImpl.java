package org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.service;

import java.util.Map;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.domain.GatewaySubscriber;
import org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.domain.GatewaySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

public class GatewaySubscriberWritePlatformServiceImpl implements GatewaySubscriberWritePlatformService {
	private final static Logger logger = LoggerFactory.getLogger(GatewaySubscriberWritePlatformServiceImpl.class);

	private final GatewaySubscriberRepository gatewaySubscriberRepository;

	public GatewaySubscriberWritePlatformServiceImpl(GatewaySubscriberRepository gatewaySubscriberRepository) {
		super();
		this.gatewaySubscriberRepository = gatewaySubscriberRepository;
	}

	@Override
	public CommandProcessingResult create(JsonCommand command) {
		try {
			GatewaySubscriber gatewaySubscriber = GatewaySubscriber.fromJson(command);
			gatewaySubscriber = gatewaySubscriberRepository.save(gatewaySubscriber);
			return new CommandProcessingResultBuilder() //
					.withCommandId(command.commandId()) //
					.withEntityId(gatewaySubscriber.getId()) //
					.build();
		} catch (final DataIntegrityViolationException dve) {
			handleDataIntegrityIssues(command, dve);
			return CommandProcessingResult.empty();
		}
	}

	@Override
	public CommandProcessingResult update(Long resourceId, JsonCommand command) {
		try {
			final GatewaySubscriber gatewaySubscriber = gatewaySubscriberRepository.findOne(resourceId);
			final Map<String, Object> changes = gatewaySubscriber.update(command);
			if (!changes.isEmpty()) {
				this.gatewaySubscriberRepository.save(gatewaySubscriber);
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
			final GatewaySubscriber gatewaySubscriber = gatewaySubscriberRepository.findOne(resourceId);
			this.gatewaySubscriberRepository.delete(gatewaySubscriber);
			this.gatewaySubscriberRepository.flush();
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