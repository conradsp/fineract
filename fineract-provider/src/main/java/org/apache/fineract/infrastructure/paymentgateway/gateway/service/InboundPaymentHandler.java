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
package org.apache.fineract.infrastructure.paymentgateway.gateway.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.data.GatewaySubscriberData;
import org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.service.GatewaySubscriberReadPlatformService;
import org.apache.fineract.infrastructure.paymentgateway.payment.domain.Payment;
import org.apache.fineract.infrastructure.paymentgateway.payment.domain.PaymentRepository;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentDirection;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentStatus;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannel;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannelRepository;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannelType;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.client.data.ClientData;
import org.apache.fineract.portfolio.client.service.ClientReadPlatformService;
import org.springframework.messaging.Message;

import com.google.gson.JsonElement;

public class InboundPaymentHandler {

	private static final String amountParameterName = "amount";
	private static final String channelNameParameterName = "channelName";
	private static final String externalRefIdParameterName = "externalRefId";
	private static final String channelRefIdParameterName = "channelRefId";
	private static final String paymentRefParameterName = "paymentRef";
	private static final String sourcePaymentAccountParameterName = "sourcePaymentAccount";
	private static final String channelMessageParameterName = "message";
	protected static final Logger logger = Logger.getLogger(InboundPaymentHandler.class.getName());
	private final FromJsonHelper fromJsonHelper;
	private final PaymentChannelRepository paymentChannelRepository;
	private final ClientReadPlatformService clientReadPlatformService;
	private final PaymentRepository paymentRepository;
	private final GatewaySubscriberReadPlatformService gatewaySubscriberReadPlatformService;
	private final PlatformSecurityContext context;

	public InboundPaymentHandler(FromJsonHelper fromJsonHelper, PaymentChannelRepository paymentChannelRepository,
			ClientReadPlatformService clientReadPlatformService,
			GatewaySubscriberReadPlatformService gatewaySubscriberReadPlatformService,
			PaymentRepository paymentRepository, PlatformSecurityContext context) {
		this.fromJsonHelper = fromJsonHelper;
		this.paymentChannelRepository = paymentChannelRepository;
		this.clientReadPlatformService = clientReadPlatformService;
		this.gatewaySubscriberReadPlatformService = gatewaySubscriberReadPlatformService;
		this.paymentRepository = paymentRepository;
		this.context = context;
	}

	public void handlePayment(Message<String> message) {
		logger.info("inbount_payment_start " + message.toString());
		String payload = message.getPayload();

		JsonElement jsonElement = fromJsonHelper.parse(payload);
		final Locale locale = this.fromJsonHelper.extractLocaleParameter(jsonElement.getAsJsonObject());
		PaymentChannel paymentChannel = null;
		List<String> errors = new ArrayList<>();

		BigDecimal amount = null;
		if (this.fromJsonHelper.parameterExists(amountParameterName, jsonElement)) {
			amount = this.fromJsonHelper.extractBigDecimalNamed(amountParameterName, jsonElement, locale);
		}
		String channelName = null;
		if (this.fromJsonHelper.parameterExists(channelNameParameterName, jsonElement)) {
			channelName = this.fromJsonHelper.extractStringNamed(channelNameParameterName, jsonElement);
		}
		String externalRefId = null;
		if (this.fromJsonHelper.parameterExists(externalRefIdParameterName, jsonElement)) {
			externalRefId = this.fromJsonHelper.extractStringNamed(externalRefIdParameterName, jsonElement);
		}
		String channelRefId = null;
		if (this.fromJsonHelper.parameterExists(channelRefIdParameterName, jsonElement)) {
			channelRefId = this.fromJsonHelper.extractStringNamed(channelRefIdParameterName, jsonElement);
		}
		String paymentRef = null;
		if (this.fromJsonHelper.parameterExists(paymentRefParameterName, jsonElement)) {
			paymentRef = this.fromJsonHelper.extractStringNamed(paymentRefParameterName, jsonElement);
		}

		String sourcePaymentAccount = null;
		if (this.fromJsonHelper.parameterExists(sourcePaymentAccountParameterName, jsonElement)) {
			sourcePaymentAccount = this.fromJsonHelper.extractStringNamed(sourcePaymentAccountParameterName,
					jsonElement);
		}

		String channelMessage = null;
		if (this.fromJsonHelper.parameterExists(channelMessageParameterName, jsonElement)) {
			channelMessage = this.fromJsonHelper.extractStringNamed(channelMessageParameterName, jsonElement);
		}

		paymentChannel = paymentChannelRepository.findByChannelName(channelName);
		if (paymentChannel == null) {
			errors.add("Invalid payment channel named: " + channelName);
		}

		ClientData clientData = null;
		if (PaymentChannelType.fromInt(paymentChannel.getChannelType()).isMobileMoneyChannel()) {
			String criteria = String.format("mobile_no = \'%s\'", sourcePaymentAccount);
			Collection<ClientData> clientDataCollection = clientReadPlatformService.retrieveAllForLookup(criteria);
			Iterator<ClientData> iterator = clientDataCollection.iterator();
			if (iterator.hasNext()) {
				clientData = iterator.next();
			}
		}

		GatewaySubscriberData gatewaySubscriberData = null;
		if (clientData != null) {
			gatewaySubscriberData = gatewaySubscriberReadPlatformService.findByClientIdAndPaymentRef(clientData.getId(),
					paymentRef);
		} else {
			errors.add("Client data with account: " + sourcePaymentAccount + " not found");
		}

		Date date = new Date();

		Payment payment = new Payment(clientData.getId(), gatewaySubscriberData.getEntityId(),
				gatewaySubscriberData.getPaymentEntity(), sourcePaymentAccount, null, amount,
				PaymentStatus.PAYMENT_PROCESSING, PaymentDirection.INCOMING, channelRefId, externalRefId,
				channelMessage, paymentChannel, this.context.getAuthenticatedUserIfPresent(), date, date, date);
		// Save Payment
		payment = paymentRepository.save(payment);

		logger.log(Level.FINE, "inbount_payment_saved " + payment.toString());
	}
}