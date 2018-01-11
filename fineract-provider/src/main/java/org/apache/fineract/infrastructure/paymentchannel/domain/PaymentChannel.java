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

package org.apache.fineract.infrastructure.paymentchannel.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.*;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.useradministration.domain.AppUser;
import org.joda.time.LocalDate;

@Entity
@Table(name = "payment_channel", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"channel_name"}, name = "unique_channel_name")})
public class PaymentChannel extends AbstractPersistableCustom<Long> {

	@Column(name = "channel_name", length = 150, nullable = false, unique = true)
	private String channelName;
	@Column(name = "channel_broker_endpoint", length = 250, nullable = false, unique = true)
	private String channelBrokerEndpoint;
	/**
	 * A value from {@link PaymentChannelType}.
	 */
	@Column(name = "channel_type", nullable = false)
	private int channelType;
	@Column(name = "is_active", nullable = false)
	private boolean isActive;
	@Column(name = "phone_number_default_region", length = 5)
	private String phoneNumberDefaultRegion;
	@Column(name = "date_created", updatable = false, nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dateCreated;
	@Column(name = "last_modified", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date lastModified;
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	private AppUser createdBy;

	public PaymentChannel() {
	}

	public PaymentChannel(String channelName, String channelBrokerEndpoint, int channelType, boolean isActive,
			String phoneNumberDefaultRegion, LocalDate dateCreated, LocalDate lastModified) {
		super();
		this.channelName = channelName;
		this.channelBrokerEndpoint = channelBrokerEndpoint;
		this.channelType = channelType;
		this.isActive = isActive;
		this.phoneNumberDefaultRegion = phoneNumberDefaultRegion;
		this.dateCreated = dateCreated.toDateTimeAtStartOfDay().toDate();
		this.createdBy = createdBy;
	}

	public PaymentChannel(final AppUser currentUser, String channelName, String channelBrokerEndpoint, int channelType,
			boolean isActive, String phoneNumberDefaultRegion, LocalDate dateCreated, LocalDate lastModified) {
		this(channelName, channelBrokerEndpoint, channelType, isActive, phoneNumberDefaultRegion, dateCreated,
				lastModified);
		this.createdBy = currentUser;
	}

	public static PaymentChannel fromJson(final JsonCommand command) {

		final String channelName = command.stringValueOfParameterNamed("channelName");
		final String channelBrokerEndpoint = command.stringValueOfParameterNamed("channelBrokerEndpoint");
		final int channelType = command.integerValueOfParameterNamed("channelType");
		final boolean isActive = command.booleanPrimitiveValueOfParameterNamed("isActive");
		final String phoneNumberDefaultRegion = command.stringValueOfParameterNamed("phoneNumberDefaultRegion");
		final LocalDate dateCreated = command.localDateValueOfParameterNamed("dateCreated");
		final LocalDate lastModified = command.localDateValueOfParameterNamed("lastModified");

		return new PaymentChannel(channelName, channelBrokerEndpoint, channelType, isActive, phoneNumberDefaultRegion,
				dateCreated, lastModified);
	}

	@PrePersist
	protected void onCreate() {
		this.lastModified = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.lastModified = new Date();
	}

	public Map<String, Object> update(final JsonCommand command) {

		final Map<String, Object> actualChanges = new LinkedHashMap<>(1);

		final String channelNameParamName = "channelName";
		if (command.isChangeInStringParameterNamed(channelNameParamName, this.channelName)) {
			final String newValue = command.stringValueOfParameterNamed(channelNameParamName);
			actualChanges.put(channelNameParamName, newValue);
			this.channelName = newValue;
		}
		final String channelBrokerEndpointParamName = "channelBrokerEndpoint";
		if (command.isChangeInStringParameterNamed(channelBrokerEndpointParamName, this.channelBrokerEndpoint)) {
			final String newValue = command.stringValueOfParameterNamed(channelBrokerEndpointParamName);
			actualChanges.put(channelBrokerEndpointParamName, newValue);
			this.channelBrokerEndpoint = newValue;
		}
		final String channelTypeParamName = "channelType";
		if (command.isChangeInIntegerParameterNamed(channelTypeParamName, this.channelType)) {
			final int newValue = command.integerValueOfParameterNamed(channelTypeParamName);
			actualChanges.put(channelTypeParamName, newValue);
			this.channelType = newValue;
		}
		final String isActiveParamName = "isActive";
		if (command.isChangeInBooleanParameterNamed(isActiveParamName, this.isActive)) {
			final boolean newValue = command.booleanPrimitiveValueOfParameterNamed(isActiveParamName);
			actualChanges.put(isActiveParamName, newValue);
			this.isActive = newValue;
		}
		final String phoneNumberDefaultRegionParamName = "phoneNumberDefaultRegion";
		if (command.isChangeInStringParameterNamed(phoneNumberDefaultRegionParamName, this.phoneNumberDefaultRegion)) {
			final String newValue = command.stringValueOfParameterNamed(phoneNumberDefaultRegionParamName);
			actualChanges.put(phoneNumberDefaultRegionParamName, newValue);
			this.phoneNumberDefaultRegion = newValue;
		}

		return actualChanges;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelBrokerEndpoint() {
		return channelBrokerEndpoint;
	}

	public void setChannelBrokerEndpoint(String channelBrokerEndpoint) {
		this.channelBrokerEndpoint = channelBrokerEndpoint;
	}

	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPhoneNumberDefaultRegion() {
		return phoneNumberDefaultRegion;
	}

	public void setPhoneNumberDefaultRegion(String phoneNumberDefaultRegion) {
		this.phoneNumberDefaultRegion = phoneNumberDefaultRegion;
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
