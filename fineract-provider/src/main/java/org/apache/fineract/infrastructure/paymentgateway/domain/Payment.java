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

package org.apache.fineract.infrastructure.paymentgateway.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.fineract.infrastructure.paymentgateway.util.DateUtil;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Payment {
	@JsonProperty("payment_id")
	private Long paymentId;
	@JsonProperty("client_id")
	private Long clientId;
	@JsonProperty("entity_id")
	private Long entityId;
	@JsonProperty("entity_type")
	private String entityType;
	@JsonProperty("action_type")
	private String actionType;
	@JsonProperty("tenant_id")
	private String tenantId;
	@JsonProperty("payment_account")
    private String paymentAccount;
	@JsonProperty("additional_data")
	private Map<String, Object> additionalData;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DEFAULT_DATE_FORMAT)
	@JsonProperty("date_created")
	private Date dateCreated;

	public Payment() {
		super();
		additionalData = new HashMap<>();
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getActionType() {
		return actionType;
	}

	public void setAction(String actionType) {
		this.actionType = actionType;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Map<String, Object> getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String key, Object value) {
		this.additionalData.put(key, value);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", clientId=" + clientId + ", entityId=" + entityId + ", entityType="
				+ entityType + ", actionType=" + actionType + ", tenantId=" + tenantId + ", paymentAccount="
				+ paymentAccount + ", additionalData=" + additionalData + ", dateCreated=" + dateCreated + "]";
	}

	
}
