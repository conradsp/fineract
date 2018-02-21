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
package org.apache.fineract.infrastructure.paymentgateway.payment.types;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by aomeri on 11/21/2015.
 */
public enum PaymentEntity {

    INVALID_ID(0, "invalid"),
    SAVINGS_ACCOUNT(1, "SAVINGS_ACCOUNT"),
    LOAN(2, "LOAN");

    private final Integer value;
    private final String name;

    private PaymentEntity(final Integer value, final String name) {
        this.value = value;
        this.name = name;
    }

    public static PaymentEntity fromInt(final Integer typeValue) {
        PaymentEntity enumeration = PaymentEntity.INVALID_ID;
        switch (typeValue) {
            case 1:
                enumeration = PaymentEntity.SAVINGS_ACCOUNT;
                break;
            case 2:
                enumeration = PaymentEntity.LOAN;
                break;
        }

        return enumeration;
    }

    @JsonValue
    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasTypeOf(final PaymentEntity type) {
        return this.value.equals(type.getValue());
    }

    public boolean isInvalId() {
        return this.value.equals(PaymentEntity.INVALID_ID.getValue());
    }

    public boolean isSavingsAccount() {
        return this.value.equals(PaymentEntity.SAVINGS_ACCOUNT.getValue());
    }

    public boolean isLoan() {
        return this.value.equals(PaymentEntity.LOAN.getValue());
    }

}