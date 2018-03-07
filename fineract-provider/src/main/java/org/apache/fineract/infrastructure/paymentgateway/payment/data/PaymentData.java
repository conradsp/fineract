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
package org.apache.fineract.infrastructure.paymentgateway.payment.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.fineract.infrastructure.paymentgateway.payment.domain.Payment;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentDirection;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentEntity;

public class PaymentData implements Comparable<PaymentData>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8065085223208148718L;
	private Long id;
	private Long clientId;
	private Long entityId;
	/**
	 * A value from {@link PaymentEntity}.
	 */
	private int paymentEntity;
	private String paymentSourceAccount;
	private String paymentDestinationAccount;
	private BigDecimal transactionAmount;
	private int paymentStatus;
	/**
	 * A value from {@link PaymentDirection}.
	 */
	private int paymentDirection;
	private String channelRefId;
	private String externalRefId;
	private Date dateCreated;
	private Date lastModified;
	private Date transactionDate;
	private String channelResponseMessage;

	public PaymentData(Payment payment) {
		this.id = payment.getId();
		this.clientId = payment.getClientId();
		this.entityId = payment.getEntityId();
		this.paymentEntity = payment.getPaymentEntity();
		this.paymentSourceAccount = payment.getPaymentSourceAccount();
		this.paymentDestinationAccount = payment.getPaymentDestinationAccount();
		this.transactionAmount = payment.getTransactionAmount();
		this.paymentStatus = payment.getPaymentStatus();
		this.paymentDirection = payment.getPaymentDirection();
		this.channelRefId = payment.getChannelRefId();
		this.externalRefId = payment.getExternalRefId();
		this.dateCreated = payment.getDateCreated();
		this.lastModified = payment.getLastModified();
		this.transactionDate = payment.getTransactionDate();
		this.channelResponseMessage = payment.getChannelResponseMessage();
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

	public void setPaymentSourceAccount(String paymentSourceAccount) {
		this.paymentSourceAccount = paymentSourceAccount;
	}

	public String getPaymentDestinationAccount() {
		return paymentDestinationAccount;
	}

	public void setPaymentDestinationAccount(String paymentDestinationAccount) {
		this.paymentDestinationAccount = paymentDestinationAccount;
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

	@Override
	public int compareTo(PaymentData obj) {
		if (obj == null) {
			return -1;
		}

		return obj.id.compareTo(this.id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "PaymentData [id=" + id + ", clientId=" + clientId + ", entityId=" + entityId + ", paymentEntity="
				+ paymentEntity + ", paymentSourceAccount=" + paymentSourceAccount + ", paymentDestinationAccount="
				+ paymentDestinationAccount + ", transactionAmount=" + transactionAmount + ", paymentStatus="
				+ paymentStatus + ", paymentDirection=" + paymentDirection + ", channelRefId=" + channelRefId
				+ ", externalRefId=" + externalRefId + ", dateCreated=" + dateCreated + ", lastModified=" + lastModified
				+ ", transactionDate=" + transactionDate + ", channelResponseMessage=" + channelResponseMessage + "]";
	}
}
