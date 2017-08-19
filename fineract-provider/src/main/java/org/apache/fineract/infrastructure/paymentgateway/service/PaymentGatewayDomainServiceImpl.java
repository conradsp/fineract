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

import org.apache.fineract.portfolio.common.BusinessEventNotificationConstants;
import org.apache.fineract.portfolio.common.service.BusinessEventListner;
import org.apache.fineract.portfolio.common.service.BusinessEventNotifierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;

public class PaymentGatewayDomainServiceImpl implements PaymentGatewayDomainService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayDomainServiceImpl.class);

    private final BusinessEventNotifierService businessEventNotifierService;

    @Autowired
    public PaymentGatewayDomainServiceImpl(final BusinessEventNotifierService businessEventNotifierService) {
        this.businessEventNotifierService = businessEventNotifierService;
    }

    @PostConstruct
    public void addListeners() {
        this.businessEventNotifierService.addBusinessEventPostListners(BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_APPROVED, new OnLoanApproved());
        this.businessEventNotifierService.addBusinessEventPostListners(BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_DISBURSAL, new OnLoanDisbursal());
        this.businessEventNotifierService.addBusinessEventPostListners(BusinessEventNotificationConstants.BUSINESS_EVENTS.LOAN_REJECTED, new OnLoanRejected());
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
            //TODO handle businessEventWasExecuted
            logger.info("businessEventWasExecuted()...........");
        }
    }

    private class OnLoanDisbursal extends PaymentGatewayBusinessEventAdapter {

        @Override
        public void businessEventWasExecuted(Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
            //TODO handle businessEventWasExecuted
            logger.info("businessEventWasExecuted()...........");
        }
    }

    private class OnLoanRejected extends PaymentGatewayBusinessEventAdapter {

        @Override
        public void businessEventWasExecuted(Map<BusinessEventNotificationConstants.BUSINESS_ENTITY, Object> businessEventEntity) {
            //TODO handle businessEventWasExecuted
            logger.info("businessEventWasExecuted()...........");
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
