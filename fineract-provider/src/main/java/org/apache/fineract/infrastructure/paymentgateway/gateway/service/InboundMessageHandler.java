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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.infrastructure.paymentgateway.gateway.util.DateUtil;
import org.apache.fineract.infrastructure.security.service.TenantDetailsService;
import org.apache.fineract.infrastructure.core.domain.FineractPlatformTenant;
import org.apache.fineract.infrastructure.core.service.ThreadLocalContextUtil;
import org.apache.fineract.infrastructure.paymentgateway.payment.domain.Payment;
import org.apache.fineract.infrastructure.paymentgateway.payment.domain.PaymentRepository;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentDirection;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentStatus;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentEntity;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.data.PaymentChannelData;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannelType;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.service.PaymentChannelReadPlatformService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.client.data.ClientData;
import org.apache.fineract.portfolio.client.service.ClientReadPlatformService;

import javax.jms.Message;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepositoryWrapper;
import org.apache.fineract.portfolio.savings.domain.SavingsAccount;
import org.apache.fineract.portfolio.savings.domain.SavingsAccountRepositoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InboundMessageHandler {
	// TODO: handle proper logging
	private static final String transactionTypeParameterName = "transactionType";
	private static final String amountParameterName = "amount";
	private static final String channelNameParameterName = "channelName";
	private static final String externalRefIdParameterName = "externalRefId";
	private static final String paymentAccountTypeParameterName = "accountType";
	private static final String paymentAccountParameterName = "paymentAccount";
	private static final String channelMessageParameterName = "message";
	private static final String paymentNoteParameterName = "paymentNote";
	private static final String mobileNoParameterName = "mobileNo";
	protected static final Logger logger = Logger.getLogger(InboundMessageHandler.class.getName());
	private final FromJsonHelper fromJsonHelper;
	private final PaymentChannelReadPlatformService paymentChannelReadPlatformService;
	private final ClientReadPlatformService clientReadPlatformService;
	private final PaymentRepository paymentRepository;
	private final PlatformSecurityContext context;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private final LoanRepositoryWrapper loanRepositoryWrapper;
	private final SavingsAccountRepositoryWrapper savingRepositoryWrapper;
	private final TenantDetailsService tenantDetailsService;

	@Autowired
	public InboundMessageHandler(FromJsonHelper fromJsonHelper, TenantDetailsService tenantDetailsService,
								 ClientReadPlatformService clientReadPlatformService,
								 PaymentChannelReadPlatformService paymentChannelReadPlatformService,
								 PaymentRepository paymentRepository, PlatformSecurityContext context,
								 PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
								 SavingsAccountRepositoryWrapper savingRepositoryWrapper, LoanRepositoryWrapper loanRepositoryWrapper) {
		this.fromJsonHelper = fromJsonHelper;
		this.paymentChannelReadPlatformService = paymentChannelReadPlatformService;
		this.clientReadPlatformService = clientReadPlatformService;
		this.paymentRepository = paymentRepository;
		this.context = context;
		this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		this.savingRepositoryWrapper = savingRepositoryWrapper;
		this.loanRepositoryWrapper = loanRepositoryWrapper;
		this.tenantDetailsService = tenantDetailsService;
	}

	public void handlePayment(String message) {
		//logger.info("inbount_payment_start " + message.toString());

		JsonElement jsonElement = fromJsonHelper.parse(message.toString());
		final Locale locale = this.fromJsonHelper.extractLocaleParameter(jsonElement.getAsJsonObject());
		PaymentChannelData paymentChannelData = null;
		List<String> errors = new ArrayList<>();

		String transactionType = null;
		if (this.fromJsonHelper.parameterExists(transactionTypeParameterName, jsonElement)) {
			transactionType = this.fromJsonHelper.extractStringNamed(transactionTypeParameterName, jsonElement);
		}
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
		String mobileNo = null;
		if (this.fromJsonHelper.parameterExists(mobileNoParameterName, jsonElement)) {
			mobileNo = this.fromJsonHelper.extractStringNamed(mobileNoParameterName, jsonElement);
		}

		String paymentAccount = null;
		if (this.fromJsonHelper.parameterExists(paymentAccountParameterName, jsonElement)) {
			paymentAccount = this.fromJsonHelper.extractStringNamed(paymentAccountParameterName,
					jsonElement);
		}

		String accountType = null;
		if (this.fromJsonHelper.parameterExists(paymentAccountTypeParameterName, jsonElement)) {
			accountType = this.fromJsonHelper.extractStringNamed(paymentAccountTypeParameterName,
					jsonElement);
		}
		// Convert the accountType to a paymentEntity type
		PaymentEntity paymentEntity;
		if (accountType.contains("SAVE")) {
			paymentEntity = PaymentEntity.SAVINGS_ACCOUNT;

		} else {
			paymentEntity = PaymentEntity.LOAN;
		}

		String channelMessage = null;
		if (this.fromJsonHelper.parameterExists(channelMessageParameterName, jsonElement)) {
			channelMessage = this.fromJsonHelper.extractStringNamed(channelMessageParameterName, jsonElement);
		}

		String paymentNote = "";
		if (this.fromJsonHelper.parameterExists(paymentNoteParameterName, jsonElement)) {
			channelMessage = this.fromJsonHelper.extractStringNamed(paymentNoteParameterName, jsonElement);
		}

		// TODO: handle multiple tenants
		FineractPlatformTenant tenant = tenantDetailsService.loadTenantById("default");
		ThreadLocalContextUtil.setTenant(tenant);
		paymentChannelData = paymentChannelReadPlatformService.findByChannelName(channelName);
		if (paymentChannelData == null) {
			errors.add("Invalid payment channel named: " + channelName);
		}

		//  Now determine the type of transaction and perform the appropriate action
		switch(transactionType) {

		}

		ClientData clientData = null;
		if (PaymentChannelType.fromInt(paymentChannelData.getChannelType()).isMobileMoneyChannel()) {
			String criteria = String.format("c.mobile_no=\'%s\'", mobileNo);
			Collection<ClientData> clientDataCollection = clientReadPlatformService.retrieveAllForLookup(criteria);
			Iterator<ClientData> iterator = clientDataCollection.iterator();
			if (iterator.hasNext()) {
				clientData = iterator.next();
			}
		}


		// Look up the account id from the account_no
		long accountId = 0;
		switch (paymentEntity) {
			case SAVINGS_ACCOUNT:
				SavingsAccount savings = this.savingRepositoryWrapper
						.findNonClosedAccountByAccountNumber(paymentAccount);
				// Verify that the client matches
				if (savings != null && (savings.getClient().getId() == clientData.getId())) {
					accountId = savings.getId();
				}
				break;
			case LOAN:
				Loan loan = this.loanRepositoryWrapper.findNonClosedLoanByAccountNumber(paymentAccount);
				// Verify that the client matches
				if (loan != null && (loan.getClient().getId() == clientData.getId())) {
					accountId = loan.getId();
				}
				break;
		}

		Date date = new Date();

		Payment payment = new Payment(clientData.getId(), accountId,
				paymentEntity, paymentAccount, null, amount,
				PaymentStatus.PAYMENT_PROCESSING, PaymentDirection.INCOMING, externalRefId,
				channelMessage, paymentChannelData.getChannelName(), this.context.getAuthenticatedUserIfPresent(), date, date, date);
		// Save Payment
		payment = paymentRepository.save(payment);
		
		 CommandWrapperBuilder builder = new CommandWrapperBuilder();

		switch (paymentEntity) {
			case SAVINGS_ACCOUNT:
				Map<String, String> savingsAccountrequestMap = new HashMap<>();
				savingsAccountrequestMap.put("dateFormat", DateUtil.SHORT_DATE_FORMAT);
				savingsAccountrequestMap.put("locale", "en"); // TODO: Find a clean way of handling locale
				savingsAccountrequestMap.put("transactionDate",
						DateUtil.formatDate(payment.getTransactionDate(), DateUtil.SHORT_DATE_FORMAT));
				savingsAccountrequestMap.put("transactionAmount", String.valueOf(payment.getTransactionAmount()));
				savingsAccountrequestMap.put("paymentTypeId", String.valueOf(paymentChannelData.getPaymentTypeId()));
				savingsAccountrequestMap.put("note", paymentNote);

				final String savingsAccountrequestJson = new Gson().toJson(savingsAccountrequestMap);
				builder.withJson(savingsAccountrequestJson);
				this.commandsSourceWritePlatformService.logCommandSource(builder.savingsAccountDeposit(accountId).build());
				break;
			case LOAN:
				Map<String, String> loanRequestMap = new HashMap<>();
				loanRequestMap.put("dateFormat", DateUtil.SHORT_DATE_FORMAT);
				loanRequestMap.put("locale", "en"); // TODO: Find a clean way of handling locale
				loanRequestMap.put("transactionDate",
						DateUtil.formatDate(payment.getTransactionDate(), DateUtil.SHORT_DATE_FORMAT));
				loanRequestMap.put("transactionAmount", String.valueOf(payment.getTransactionAmount()));
				loanRequestMap.put("paymentTypeId", String.valueOf(paymentChannelData.getPaymentTypeId()));
				loanRequestMap.put("note", paymentNote);

				final String loanRequestJson = new Gson().toJson(loanRequestMap);
				builder.withJson(loanRequestJson);
				this.commandsSourceWritePlatformService.logCommandSource(builder.loanRepaymentTransaction(accountId).build());
				break;
			case FIXED_SAVINGS_ACCOUNT:
				Map<String, String> fixedSavingsAccountequestMap = new HashMap<>();
				fixedSavingsAccountequestMap.put("dateFormat", DateUtil.SHORT_DATE_FORMAT);
				fixedSavingsAccountequestMap.put("locale", "en"); // TODO: Find a clean way of handling locale
				fixedSavingsAccountequestMap.put("transactionDate",
						DateUtil.formatDate(payment.getTransactionDate(), DateUtil.SHORT_DATE_FORMAT));
				fixedSavingsAccountequestMap.put("transactionAmount", String.valueOf(payment.getTransactionAmount()));
				fixedSavingsAccountequestMap.put("paymentTypeId", String.valueOf(paymentChannelData.getPaymentTypeId()));
				fixedSavingsAccountequestMap.put("note", paymentNote);

				final String fixedSavingsAccountequestJson = new Gson().toJson(fixedSavingsAccountequestMap);
				builder.withJson(fixedSavingsAccountequestJson);
				this.commandsSourceWritePlatformService.logCommandSource(builder.fixedDepositAccountDeposit(accountId).build());
				break;
			default:
				errors.add("Invalid payment entity");
				break;
		}

		logger.log(Level.FINE, "inbound_payment_saved " + payment.toString());
	}
}
