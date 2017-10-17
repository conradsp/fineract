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

import org.apache.fineract.infrastructure.paymentgateway.domain.Payment;
import org.apache.fineract.portfolio.common.BusinessEventNotificationConstants;
import org.apache.fineract.portfolio.common.service.BusinessEventListner;
import org.apache.fineract.portfolio.common.service.BusinessEventNotifierService;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.Date;
import java.util.Map;

@Service
public class PaymentGatewayDomainServiceImpl implements PaymentGatewayDomainService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayDomainServiceImpl.class);

    private final BusinessEventNotifierService businessEventNotifierService;
    
    private final PaymentGateway paymentGateway;

    @Autowired
    public PaymentGatewayDomainServiceImpl(final BusinessEventNotifierService businessEventNotifierService,
    		final PaymentGateway paymentGateway) {
        this.businessEventNotifierService = businessEventNotifierService;
        this.paymentGateway = paymentGateway;
    }

    @PostConstruct
    public void addListeners() {
        this.businessEventNotifierService.addBusinessEventPostListners(BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_APPROVED, new OnLoanApproved());
        this.businessEventNotifierService.addBusinessEventPostListners(BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_DISBURSAL, new OnLoanDisbursal());
        this.businessEventNotifierService.addBusinessEventPostListners(BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_UNDO_DISBURSAL, new OnLoanUndoDisbursal());
        this.businessEventNotifierService.addBusinessEventPostListners(BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_MAKE_REPAYMENT, new OnLoanRepayment());
    }

    private abstract class PaymentGatewayBusinessEventAdapter implements BusinessEventListner {

        @Override
        public void businessEventToBeExecuted(Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
            //Nothing to do
        }
    }


    private class OnLoanApproved extends PaymentGatewayBusinessEventAdapter {

        @Override
        public void businessEventWasExecuted(Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
            //paymentGateway.processPayment("Processing payment test: OnLoanApproved");
            logger.info("businessEventWasExecuted()...........");
        }
    }

    private class OnLoanDisbursal extends PaymentGatewayBusinessEventAdapter {

        @Override
        public void businessEventWasExecuted(Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
            //TODO handle businessEventWasExecuted
            logger.info("businessEventWasExecuted()...........");
            logger.info("businessEventEntity..........."+ businessEventEntity);
            
            Object entity = businessEventEntity.get(BusinessEventNotificationConstants.BUSINESS_ENTITY.LOAN);
            logger.info("Not null..........."+(entity != null));
            if (entity != null) {
                Loan loan = (Loan) entity;
                Payment payment =new Payment();
                payment.setPaymentId(1l);
                payment.setClientId(loan.getClientId());
                payment.setEntityId(loan.getId());
                payment.setEntityType("Loan");
                payment.setAction("LoanDisbursal");
                payment.setDateCreated(new Date());
                
                logger.info("Object..........."+payment.toString());
                paymentGateway.processPayment(payment);
            }
        }
    }

    private class OnLoanUndoDisbursal extends PaymentGatewayBusinessEventAdapter {

        @Override
        public void businessEventWasExecuted(Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
            //TODO handle businessEventWasExecuted
        	    logger.info("businessEventWasExecuted()...........");
            logger.info("businessEventEntity..........."+ businessEventEntity);
                
            Object entity = businessEventEntity.get(BusinessEventNotificationConstants.BUSINESS_ENTITY.LOAN);
            logger.info("Not null..........."+(entity != null));
            if (entity != null) {
                Loan loan = (Loan) entity;
                Payment payment =new Payment();
                payment.setPaymentId(1l);
                payment.setClientId(loan.getClientId());
                payment.setEntityId(loan.getId());
                payment.setEntityType("Loan");
                payment.setAction("UndoLoanDisbursal");
                paymentGateway.processPayment(payment);
            }
        }
    }

    private class OnLoanRepayment extends PaymentGatewayBusinessEventAdapter {

        @Override
        public void businessEventWasExecuted(Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
            //TODO handle businessEventWasExecuted
            logger.info("businessEventWasExecuted()...........");
        }
    }
}
