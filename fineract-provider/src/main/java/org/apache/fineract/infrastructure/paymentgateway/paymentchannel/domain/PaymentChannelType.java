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

package org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Antony on 18/10/2017.
 */
public enum PaymentChannelType {
	
    INVALID_ID(0, "INVALID_ID"),
    MOBILE_MONEY_CHANNEL(1, "MOBILE_MONEY_CHANNEL"),
    BANKING_CHANNEL(2, "BANKING_CHANNEL"),
    EMAIL_MONEY_CHANNEL(3, "EMAIL_MONEY_CHANNEL");

    private final Integer value;
    private final String code;

    PaymentChannelType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public static PaymentChannelType fromInt(final Integer typeValue) {
        PaymentChannelType enumeration = PaymentChannelType.INVALID_ID;
        switch (typeValue) {
            case 1:
                enumeration = PaymentChannelType.MOBILE_MONEY_CHANNEL;
                break;
            case 2:
                enumeration = PaymentChannelType.BANKING_CHANNEL;
                break;
            case 3:
                enumeration = PaymentChannelType.EMAIL_MONEY_CHANNEL;
                break;
        }

        return enumeration;
    }

    @JsonValue
    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public boolean hasTypeOf(final PaymentChannelType type) {
        return this.value.equals(type.getValue());
    }

    public boolean isInvalId() {
        return this.value.equals(PaymentChannelType.INVALID_ID.getValue());
    }

    public boolean isMobileMoneyChannel() {
        return this.value.equals(PaymentChannelType.MOBILE_MONEY_CHANNEL.getValue());
    }

    public boolean isBankingChannel() {
        return this.value.equals(PaymentChannelType.BANKING_CHANNEL.getValue());
    }

    public boolean isEmailMoneyChannel() {
        return this.value.equals(PaymentChannelType.EMAIL_MONEY_CHANNEL.getValue());
    }
}
