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

package org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentEntity;
import org.apache.fineract.useradministration.domain.AppUser;

@Entity
@Table(name = "payment_gateway_subscriber")
public class GatewaySubscriber extends AbstractPersistableCustom<Long> {
	private static final long serialVersionUID = 821716679287590971L;
	@Column(name = "client_id")
	private Long clientId;
	@Column(name = "entity_id")
	private Long entityId;
	/**
	 * A value from {@link PaymentEntity}.
	 */
	@Column(name = "payment_entity")
	private int paymentEntity;
	@Column(name = "payment_ref")
	private String paymentRef;
	@Column(name = "date_created", updatable = false, nullable = false)
	private Date dateCreated;
	@Column(name = "last_modified", nullable = false)
	private Date lastModified;
	@JoinColumn(name = "user_id", nullable = true)
	private AppUser createdBy;

	public GatewaySubscriber(Long clientId, Long entityId, int paymentEntity, String paymentRef) {
		super();
		this.clientId = clientId;
		this.entityId = entityId;
		this.paymentEntity = paymentEntity;
		this.paymentRef = paymentRef;
	}

	public static GatewaySubscriber fromJson(final JsonCommand command) {

		final Long clientId = command.longValueOfParameterNamed("clientId");
		final Long entityId = command.longValueOfParameterNamed("entityId");
		final int paymentEntity = command.integerValueOfParameterNamed("paymentEntity");
		final String paymentRef = command.stringValueOfParameterNamed("paymentRef");

		return new GatewaySubscriber(clientId, entityId, paymentEntity, paymentRef);
	}
	
	public Map<String, Object> update(final JsonCommand command) {

		final Map<String, Object> actualChanges = new LinkedHashMap<>(1);

		final String clientIdParamName = "clientId";
		if (command.isChangeInLongParameterNamed(clientIdParamName, this.clientId)) {
			final Long newValue = command.longValueOfParameterNamed(clientIdParamName);
			actualChanges.put(clientIdParamName, newValue);
			this.clientId = newValue;
		}
		final String entityIdParamName = "entityId";
		if (command.isChangeInLongParameterNamed(entityIdParamName, this.entityId)) {
			final Long newValue = command.longValueOfParameterNamed(entityIdParamName);
			actualChanges.put(entityIdParamName, newValue);
			this.entityId = newValue;
		}
		final String paymentEntityParamName = "channelType";
		if (command.isChangeInIntegerParameterNamed(paymentEntityParamName, this.paymentEntity)) {
			final int newValue = command.integerValueOfParameterNamed(paymentEntityParamName);
			actualChanges.put(paymentEntityParamName, newValue);
			this.paymentEntity = newValue;
		}
		final String paymentRefParamName = "paymentRef";
		if (command.isChangeInStringParameterNamed(paymentRefParamName, this.paymentRef)) {
			final String newValue = command.stringValueOfParameterNamed(paymentRefParamName);
			actualChanges.put(paymentRefParamName, newValue);
			this.paymentRef = newValue;
		}

		return actualChanges;
	}
	
	@PrePersist
	protected void onCreate() {
		Date date = new Date();
		this.dateCreated = date;
		this.lastModified = date;
	}

	@PreUpdate
	protected void onUpdate() {
		this.lastModified = new Date();
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

	public int getPaymentEntity() {
		return paymentEntity;
	}

	public void setPaymentEntity(int paymentEntity) {
		this.paymentEntity = paymentEntity;
	}

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public AppUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(AppUser createdBy) {
		this.createdBy = createdBy;
	}
}
