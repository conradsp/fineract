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
 * Created by Antony on 2/10/2016.
 */
public enum PaymentStatus {
    INVALID_ID(0, "INVALID_ID"),
    PAYMENT_COMPLETE(1, "PAYMENT_COMPLETE"),
    PAYMENT_PENDING(2, "PAYMENT_PENDING"),
    PAYMENT_ON_HOLD(3, "PAYMENT_ON_HOLD"),
    PAYMENT_CANCELLED(4, "PAYMENT_CANCELLED"),
    PAYMENT_REVERSED(5, "PAYMENT_REVERSED"),
    PAYMENT_FAILED(6, "PAYMENT_FAILED"),
    PAYMENT_PROCESSING(7, "PAYMENT_PROCESSING");

    private final Integer value;
    private final String code;

    PaymentStatus(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public static PaymentStatus fromInt(final Integer typeValue) {
        PaymentStatus enumeration = PaymentStatus.INVALID_ID;
        switch (typeValue) {
            case 1:
                enumeration = PaymentStatus.PAYMENT_COMPLETE;
                break;
            case 2:
                enumeration = PaymentStatus.PAYMENT_PENDING;
                break;
            case 3:
                enumeration = PaymentStatus.PAYMENT_ON_HOLD;
                break;
            case 4:
                enumeration = PaymentStatus.PAYMENT_CANCELLED;
                break;
            case 5:
                enumeration = PaymentStatus.PAYMENT_REVERSED;
                break;
            case 6:
                enumeration = PaymentStatus.PAYMENT_FAILED;
                break;
            case 7:
                enumeration = PaymentStatus.PAYMENT_PROCESSING;
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

    public boolean hasTypeOf(final PaymentStatus type) {
        return this.value.equals(type.getValue());
    }

    public boolean isInvalId() {
        return this.value.equals(PaymentStatus.INVALID_ID.getValue());
    }

    public boolean isPaymentComplete() {
        return this.value.equals(PaymentStatus.PAYMENT_COMPLETE.getValue());
    }

    public boolean isPaymentPending() {
        return this.value.equals(PaymentStatus.PAYMENT_PENDING.getValue());
    }

    public boolean isPaymentOnHold() {
        return this.value.equals(PaymentStatus.PAYMENT_ON_HOLD.getValue());
    }

    public boolean isPaymentCancelled() {
        return this.value.equals(PaymentStatus.PAYMENT_CANCELLED.getValue());
    }

    public boolean isPaymentReversed() {
        return this.value.equals(PaymentStatus.PAYMENT_REVERSED.getValue());
    }

    public boolean isPaymentFailed() {
        return this.value.equals(PaymentStatus.PAYMENT_FAILED.getValue());
    }

    public boolean isPaymentProcessing() {
        return this.value.equals(PaymentStatus.PAYMENT_PROCESSING.getValue());
    }
}
