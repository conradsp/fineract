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

package org.apache.fineract.infrastructure.paymentgateway.paymentgateway.data;

import java.io.Serializable;
import java.util.Date;

import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.domain.PaymentGateway;
import org.apache.fineract.infrastructure.paymentgateway.paymentgateway.domain.PaymentChannelType;
import org.apache.fineract.useradministration.domain.AppUser;

public class PaymentGatewayData implements Serializable {
	private static final long serialVersionUID = 8966206292377691396L;
	private Long id;
	private String channelName;
	private String channelBrokerEndpoint;
	/**
	 * A value from {@link PaymentChannelType}.
	 */
	private boolean isActive;
	private String gatewayUrl;
	private Long paymentTypeId;
	private Long paymentGatewayUser;

	public PaymentGatewayData(PaymentGateway paymentGateway) {
		super();
		this.gatewayUrl = paymentGateway.getGatewayUrl();
		this.paymentGatewayUser = paymentGateway.getUserId();
		this.isActive = paymentGateway.isActive();
		this.paymentTypeId = paymentGateway.getPaymentType();
	}

	public PaymentGatewayData() {
		super();
	}

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public Long getPaymentGatewayUser() {
		return paymentGatewayUser;
	}

	public void setUserId(Long paymentGatewayUser) {
		this.paymentGatewayUser = paymentGatewayUser;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Long getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(Long paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}


}
