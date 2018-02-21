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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.infrastructure.paymentgateway.payment.domain.Payment;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannel;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannelRepository;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannelType;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.service.PaymentChannelReadPlatformService;
import org.apache.fineract.portfolio.client.data.ClientData;
import org.apache.fineract.portfolio.client.service.ClientReadPlatformService;
import org.springframework.messaging.Message;

import com.google.gson.JsonElement;

public class InboundPaymentHandler {

	private static final String amountParameterName = "amount";
	private static final String channelNameParameterName = "channelName";
	private static final String externalChannelRefIdParameterName = "externalChannelRefId";
	private static final String channelRefIdParameterName = "channelRefId";
	private static final String destinationPaymentAccountParameterName = "destinationPaymentAccount";
	private static final String sourcePaymentAccountParameterName = "sourcePaymentAccount";
	private static final String paymentAccountTypeParameterName = "paymentAccountType";
	protected static final Logger logger = Logger.getLogger(InboundPaymentHandler.class.getName());
	private final FromJsonHelper fromJsonHelper;
	private final PaymentChannelRepository paymentChannelRepository;
	private final ClientReadPlatformService clientReadPlatformService;

	public InboundPaymentHandler(FromJsonHelper fromJsonHelper, PaymentChannelRepository paymentChannelRepository,
			ClientReadPlatformService clientReadPlatformService) {
		this.fromJsonHelper = fromJsonHelper;
		this.paymentChannelRepository = paymentChannelRepository;
		this.clientReadPlatformService = clientReadPlatformService;
	}

	public void handlePayment(Message<String> message) {
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
		String externalChannelRefId = null;
		if (this.fromJsonHelper.parameterExists(externalChannelRefIdParameterName, jsonElement)) {
			externalChannelRefId = this.fromJsonHelper.extractStringNamed(externalChannelRefIdParameterName,
					jsonElement);
		}
		String channelRefId = null;
		if (this.fromJsonHelper.parameterExists(channelRefIdParameterName, jsonElement)) {
			channelRefId = this.fromJsonHelper.extractStringNamed(channelRefIdParameterName, jsonElement);
		}
		String destinationPaymentAccount = null;
		if (this.fromJsonHelper.parameterExists(destinationPaymentAccountParameterName, jsonElement)) {
			destinationPaymentAccount = this.fromJsonHelper.extractStringNamed(destinationPaymentAccountParameterName, jsonElement);
		}
		
		String sourcePaymentAccount = null;
		if (this.fromJsonHelper.parameterExists(sourcePaymentAccountParameterName, jsonElement)) {
			sourcePaymentAccount = this.fromJsonHelper.extractStringNamed(sourcePaymentAccountParameterName, jsonElement);
		}
		
		Integer paymentAccountType = null;
		if (this.fromJsonHelper.parameterExists(paymentAccountTypeParameterName, jsonElement)) {
			paymentAccountType = this.fromJsonHelper.extractIntegerNamed(paymentAccountTypeParameterName, jsonElement, locale);
		}
		
		paymentChannel = paymentChannelRepository.findByChannelName(channelName);
		if (paymentChannel == null) {
			errors.add("Invalid payment channel named: " + channelName);
		}
		
		ClientData clientData = null;
		if (PaymentChannelType.fromInt(paymentChannel.getChannelType()).isMobileMoneyChannel()) {
			String criteria = String.format("mobile_no = \'%s\'", destinationPaymentAccount);
			Collection<ClientData> clientDataCollection = clientReadPlatformService.retrieveAllForLookup(criteria);
			Iterator<ClientData> iterator = clientDataCollection.iterator();
			if(iterator.hasNext()) {
				clientData = iterator.next();
			}
		}
		if(clientData == null) {
			errors.add("Client data with account: " + destinationPaymentAccount + " not found");
		}

//        Payment payment = new Payment(Long clientId, Long entityId, int paymentEntity, String paymentSourceAccount, String paymentDestinationAccount, BigDecimal transactionAmount,
//        int paymentStatus, int paymentDirection, String externalId, String channelResponseMessage,
//                PaymentChannel paymentChannel, AppUser createdBy, Date dateCreated, Date transactionDate,
//                Date lastModified);

		System.out.println("Printing the message:");
		System.out.println(message);

		logger.info("Printing the message:");
		logger.info(message.toString());
	}
}
