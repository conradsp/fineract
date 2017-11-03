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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.organisation.office.domain.Office;
import org.apache.fineract.portfolio.client.domain.ClientStatus;
import org.joda.time.LocalDate;

@Entity
@Table(name = "payment_channel", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "channel_name" }, name = "unique_channel_name"),
		@UniqueConstraint(columnNames = { "channel_endpoint_tag" }, name = "unique_channel_endpoint_tag") })
public class PaymentChannel extends AbstractPersistableCustom<Long> {

	@Column(name = "channel_name", length = 150,  nullable = false, unique = true)
	private String channelName;
	@Column(name = "channel_endpoint_tag", length = 250, nullable = false, unique = true)
	private String channelEndpointTag;
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

	public PaymentChannel(String channelName, String channelEndpointTag, int channelType,
			boolean isActive, String phoneNumberDefaultRegion, LocalDate dateCreated, LocalDate lastModified) {
		super();
		this.channelName = channelName;
		this.channelEndpointTag = channelEndpointTag;
		this.channelType = channelType;
		this.isActive = isActive;
		this.phoneNumberDefaultRegion = phoneNumberDefaultRegion;
		this.dateCreated = dateCreated.toDateTimeAtStartOfDay().toDate();
		this.lastModified = lastModified.toDateTimeAtStartOfDay().toDate();
	}

	public static PaymentChannel fromJson(final JsonCommand command) {

		final String channelName = command.stringValueOfParameterNamed("channelName");
		final String channelEndpointTag = command.stringValueOfParameterNamed("channelEndpointTag");
		final int channelType = command.integerValueOfParameterNamed("channelType");
		final boolean isActive = command.booleanPrimitiveValueOfParameterNamed("isActive");
		final String phoneNumberDefaultRegion = command.stringValueOfParameterNamed("phoneNumberDefaultRegion");
		final LocalDate dateCreated = command.localDateValueOfParameterNamed("dateCreated");
		final LocalDate lastModified = command.localDateValueOfParameterNamed("lastModified");
		
		return new PaymentChannel(channelName, channelEndpointTag, channelType, isActive, phoneNumberDefaultRegion,
				dateCreated, lastModified);
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelEndpointTag() {
		return channelEndpointTag;
	}

	public void setChannelEndpointTag(String channelEndpointTag) {
		this.channelEndpointTag = channelEndpointTag;
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

}
