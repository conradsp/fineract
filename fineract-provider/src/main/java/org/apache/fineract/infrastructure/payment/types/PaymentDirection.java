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
package org.apache.fineract.infrastructure.payment.types;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Antony on 2/11/2016.
 */
public enum PaymentDirection {
    INVALID_ID(0, "INVALID_ID"),
    INCOMING(1, "INCOMING"),
    OUTGOING(2, "OUTGOING");

    private final Integer value;
    private final String code;

    PaymentDirection(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public static PaymentDirection fromInt(final Integer typeValue) {
        PaymentDirection enumeration = PaymentDirection.INVALID_ID;
        switch (typeValue) {
            case 1:
                enumeration = PaymentDirection.INCOMING;
                break;
            case 2:
                enumeration = PaymentDirection.OUTGOING;
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

    public boolean hasTypeOf(final PaymentDirection type) {
        return this.value.equals(type.getValue());
    }

    public boolean isInvalidPaymentType() {
        return this.value.equals(PaymentDirection.INVALID_ID.getValue());
    }

    public boolean isIncomingPayment() {
        return this.value.equals(PaymentDirection.INCOMING.getValue());
    }

    public boolean isOutgoingPayment() {
        return this.value.equals(PaymentDirection.OUTGOING.getValue());
    }
}
