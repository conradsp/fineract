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
package org.apache.fineract.infrastructure.paymentgateway.payment.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentDirection;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentEntity;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentStatus;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannel;
import org.apache.fineract.useradministration.domain.AppUser;

@Entity
@Table(name = "payment")
public class Payment extends AbstractPersistableCustom<Long> {

	private static final long serialVersionUID = 3094416058294465789L;
	@Column(name = "client_id")
	private Long clientId;
	@Column(name = "entity_id")
	private Long entityId;
	/**
	 * A value from {@link PaymentEntity}.
	 */
	@Column(name = "payment_entity")
	private int paymentEntity;
	@Column(name = "payment_source_account", length = 100)
	private String paymentSourceAccount;
	@Column(name = "payment_destination_account", length = 100)
	private String paymentDestinationAccount;
	@Column(name = "transaction_amount", scale = 6, precision = 19)
	private BigDecimal transactionAmount;
	@Column(name = "payment_status")
	private int paymentStatus;
	/**
	 * A value from {@link PaymentDirection}.
	 */
	@Column(name = "payment_direction")
	private int paymentDirection;
	@Column(name = "channel_ref_id", length = 150)
	private String channelRefId;
	@Column(name = "external_ref_id", length = 150)
	private String externalRefId;
	@Column(name = "date_created", updatable = false, nullable = false)
	private Date dateCreated;
	@Column(name = "last_modified", nullable = false)
	private Date lastModified;
	@Column(name = "transaction_date")
	private Date transactionDate;
	@Column(name = "channel_response_message", length = 250)
	private String channelResponseMessage;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_channel_id", nullable = false)
	private PaymentChannel paymentChannel;
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	private AppUser createdBy;

	public Payment(Long clientId, Long entityId, PaymentEntity paymentEntity, String paymentSourceAccount,
			String paymentDestinationAccount, BigDecimal transactionAmount, PaymentStatus paymentStatus,
			PaymentDirection paymentDirection, PaymentChannel paymentChannel, AppUser createdBy) {
		this.clientId = clientId;
		this.entityId = entityId;
		this.paymentEntity = paymentEntity.getValue();
		this.paymentSourceAccount = paymentSourceAccount;
		this.paymentDestinationAccount = paymentDestinationAccount;
		this.transactionAmount = transactionAmount;
		this.paymentStatus = paymentStatus.getValue();
		this.paymentDirection = paymentDirection.getValue();
		this.paymentChannel = paymentChannel;
		this.createdBy = createdBy;
	}

	public Payment(Long clientId, Long entityId, PaymentEntity paymentEntity, String paymentSourceAccount,
			String paymentDestinationAccount, BigDecimal transactionAmount, PaymentStatus paymentStatus,
			PaymentDirection paymentDirection, String channelRefId, String externalRefId, String channelResponseMessage,
			PaymentChannel paymentChannel, AppUser createdBy, Date dateCreated, Date transactionDate,
			Date lastModified) {
		this(clientId, entityId, paymentEntity, paymentSourceAccount, paymentDestinationAccount, transactionAmount,
				paymentStatus, paymentDirection, paymentChannel, createdBy);

		this.dateCreated = dateCreated;
		this.lastModified = lastModified;
		this.transactionDate = transactionDate;
		this.channelRefId = channelRefId;
		this.externalRefId = externalRefId;
		this.channelResponseMessage = channelResponseMessage;
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

	/**
	 * A value from
	 * {@link org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentEntity}.
	 */
	public int getPaymentEntity() {
		return paymentEntity;
	}

	public void setPaymentEntity(int paymentEntity) {
		this.paymentEntity = paymentEntity;
	}

	public String getPaymentSourceAccount() {
		return paymentSourceAccount;
	}

	public void setPaymentSourceAccount(String paymentAccount) {
		this.paymentSourceAccount = paymentAccount;
	}

	public String getPaymentDestinationAccount() {
		return paymentDestinationAccount;
	}

	public void setPaymentDestinationAccount(String paymentAccount) {
		this.paymentDestinationAccount = paymentAccount;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	/**
	 * A value from
	 * {@link org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentStatus}.
	 */
	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	/**
	 * A value from
	 * {@link org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentDirection}.
	 */
	public int getPaymentDirection() {
		return paymentDirection;
	}

	public void setPaymentDirection(int paymentDirection) {
		this.paymentDirection = paymentDirection;
	}

	public String getChannelRefId() {
		return channelRefId;
	}

	public void setChannelRefId(String channelRefId) {
		this.channelRefId = channelRefId;
	}

	public String getExternalRefId() {
		return externalRefId;
	}

	public void setExternalRefId(String externalRefId) {
		this.externalRefId = externalRefId;
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

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getChannelResponseMessage() {
		return channelResponseMessage;
	}

	public void setChannelResponseMessage(String channelResponseMessage) {
		this.channelResponseMessage = channelResponseMessage;
	}

	public PaymentChannel getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public AppUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(AppUser createdBy) {
		this.createdBy = createdBy;
	}
}
