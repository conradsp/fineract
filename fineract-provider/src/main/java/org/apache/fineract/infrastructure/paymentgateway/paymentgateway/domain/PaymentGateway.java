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

package org.apache.fineract.infrastructure.paymentgateway.paymentgateway.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.portfolio.paymenttype.domain.PaymentType;
import org.apache.fineract.useradministration.domain.AppUser;
import org.joda.time.LocalDate;

@Entity
@Table(name = "payment_gateway_config")
public class PaymentGateway extends AbstractPersistableCustom<Long> {

	private static final long serialVersionUID = -7898973749472818660L;
	@Column(name = "gateway_url", length = 150, nullable = false, unique = true)
	private String gatewayUrl;
	@Column(name = "is_active", nullable = false)
	private boolean isActive;
	@Column(name = "payment_type_id", nullable = false)
	private Long paymentType;
	@Column(name = "user_id", nullable = true)
	private Long userId;

	public PaymentGateway() {
	}

	public PaymentGateway(String gatewayUrl, boolean isActive) {
		super();
		this.gatewayUrl = gatewayUrl;
		this.isActive = isActive;
	}

	public PaymentGateway(String gatewayUrl, boolean isActive, PaymentType paymentType, final AppUser userId) {
		this(gatewayUrl, isActive);
	}

	public static PaymentGateway fromJson(final JsonCommand command) {

		final String gatewayUrl = command.stringValueOfParameterNamed("gatewayUrl");
		final boolean isActive = command.booleanPrimitiveValueOfParameterNamed("isActive");
		final Long paymentType = command.longValueOfParameterNamed("paymentType");
		final Long userId = command.longValueOfParameterNamed("userId");

		return new PaymentGateway(gatewayUrl, isActive);
	}

	public Map<String, Object> update(final JsonCommand command) {

		final Map<String, Object> actualChanges = new LinkedHashMap<>(1);

		final String channelNameParamName = "gatewayUrl";
		if (command.isChangeInStringParameterNamed(channelNameParamName, this.gatewayUrl)) {
			final String newValue = command.stringValueOfParameterNamed(channelNameParamName);
			actualChanges.put(channelNameParamName, newValue);
			this.gatewayUrl = newValue;
		}
		final String isActiveParamName = "isActive";
		if (command.isChangeInBooleanParameterNamed(isActiveParamName, this.isActive)) {
			final boolean newValue = command.booleanPrimitiveValueOfParameterNamed(isActiveParamName);
			actualChanges.put(isActiveParamName, newValue);
			this.isActive = newValue;
		}

		final String paymentTypeName = "paymentType";
		if (command.isChangeInLongParameterNamed(paymentTypeName, this.paymentType)) {
			final Long newValue = command.longValueOfParameterNamed(paymentTypeName);
			actualChanges.put(paymentTypeName, newValue);
			this.paymentType = newValue;
		}

		final String userIdName = "userId";
		if (command.isChangeInLongParameterNamed(userIdName, this.userId)) {
			final Long newValue = command.longValueOfParameterNamed(userIdName);
			actualChanges.put(userIdName, newValue);
			this.userId = newValue;
		}

		return actualChanges;
	}

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Long getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Long paymentType) {
		this.paymentType = paymentType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setCreatedBy(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("gatewayUrl", gatewayUrl)
				.append("isActive", isActive)
				.append("userId", userId)
				.append("active", isActive()).toString();
	}
}
