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
package org.apache.fineract.infrastructure.paymentgateway.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.fineract.infrastructure.payment.domain.Payment;
import org.apache.fineract.infrastructure.payment.domain.PaymentRepository;
import org.apache.fineract.infrastructure.payment.types.PaymentDirection;
import org.apache.fineract.infrastructure.payment.types.PaymentEntity;
import org.apache.fineract.infrastructure.payment.types.PaymentStatus;
import org.apache.fineract.infrastructure.paymentchannel.domain.PaymentChannel;
import org.apache.fineract.infrastructure.paymentgateway.config.OutboundChannelHelper;
import org.apache.fineract.infrastructure.paymentgateway.util.HashUtil;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.common.BusinessEventNotificationConstants;
import org.apache.fineract.portfolio.common.service.BusinessEventListner;
import org.apache.fineract.portfolio.common.service.BusinessEventNotifierService;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransaction;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransactionType;
import org.apache.fineract.portfolio.paymentdetail.domain.PaymentDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class PaymentGatewayDomainServiceImpl implements PaymentGatewayDomainService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayDomainServiceImpl.class);

	private final BusinessEventNotifierService businessEventNotifierService;
	private final OutboundChannelHelper outboundChannelHelper;
	private final PlatformSecurityContext securityContext;
	private final PaymentRepository paymentRepository;
	private final HashUtil hashUtil;

	@Autowired
	public PaymentGatewayDomainServiceImpl(final BusinessEventNotifierService businessEventNotifierService,
			OutboundChannelHelper outboundChannelHelper, PlatformSecurityContext securityContext,
			PaymentRepository paymentRepository, HashUtil hashUtil) {
		this.businessEventNotifierService = businessEventNotifierService;
		this.outboundChannelHelper = outboundChannelHelper;
		this.securityContext = securityContext;
		this.paymentRepository = paymentRepository;
		this.hashUtil = hashUtil;
	}

	@PostConstruct
	public void addListeners() {
		this.businessEventNotifierService.addBusinessEventPostListners(
				BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_APPROVED, new OnLoanApproved());
		this.businessEventNotifierService.addBusinessEventPostListners(
				BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_DISBURSAL, new OnLoanDisbursal());
		this.businessEventNotifierService.addBusinessEventPostListners(
				BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_UNDO_DISBURSAL, new OnLoanUndoDisbursal());
		this.businessEventNotifierService.addBusinessEventPostListners(
				BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_MAKE_REPAYMENT, new OnLoanRepayment());
	}

	private abstract class PaymentGatewayBusinessEventAdapter implements BusinessEventListner {

		@Override
		public void businessEventToBeExecuted(
				Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
			// Nothing to do
		}
	}

	private class OnLoanApproved extends PaymentGatewayBusinessEventAdapter {

		@Override
		public void businessEventWasExecuted(
				Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
			// TODO handle businessEventWasExecuted
		}
	}

	private class OnLoanDisbursal extends PaymentGatewayBusinessEventAdapter {

		@Override
		public void businessEventWasExecuted(
				Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
			Object loanEntity = businessEventEntity.get(BusinessEventNotificationConstants.BUSINESS_ENTITY.LOAN);
			if (loanEntity != null) {
				Loan loan = (Loan) loanEntity;
				LoanTransaction loanTransaction = null;

				List<LoanTransaction> loanTransactions = loan.getLoanTransactions();
				//Hack: to get exact disburse transaction
				List<LoanTransaction> filteredLoanTransactions = loanTransactions.stream()
						.filter(e -> !e.isReversed() && e.getTypeOf() == LoanTransactionType.DISBURSEMENT)
						.collect(Collectors.toList());

				loanTransaction = filteredLoanTransactions.get(0);
				PaymentDetail paymentDetail = loanTransaction.getPaymentDetail();
				PaymentChannel paymentChannel = paymentDetail.getPaymentChannel();

				Payment payment = new Payment(loan.getClientId(), loan.getId(), PaymentEntity.LOAN.getValue(),
						paymentDetail.getAccountNumber(), loanTransaction.getAmount(loan.getCurrency()).getAmount(),
						PaymentStatus.PAYMENT_PROCESSING.getValue(), PaymentDirection.OUTGOING.getValue(),
						paymentChannel, securityContext.getAuthenticatedUserIfPresent());

				payment = paymentRepository.save(payment);
				Map<String, Object> paymentMap = new HashMap<>();
				paymentMap.put("transactionReference", hashUtil.hashEncodeId(payment.getId()));
				paymentMap.put("paymentAccount", payment.getPaymentAccount());
				paymentMap.put("transactionAmount", payment.getTransactionAmount());

				final String jsonPayment = new Gson().toJson(paymentMap);
				// send payment to queue
				outboundChannelHelper.sendMessage(paymentChannel.getChannelName(), jsonPayment);
			}
		}
	}

	private class OnLoanUndoDisbursal extends PaymentGatewayBusinessEventAdapter {

		@Override
		public void businessEventWasExecuted(
				Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
			// TODO handle businessEventWasExecuted
		}
	}

	private class OnLoanRepayment extends PaymentGatewayBusinessEventAdapter {

		@Override
		public void businessEventWasExecuted(
				Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
			// TODO handle businessEventWasExecuted
		}
	}
}
