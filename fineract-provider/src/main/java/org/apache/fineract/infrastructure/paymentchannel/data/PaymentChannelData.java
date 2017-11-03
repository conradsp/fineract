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

package org.apache.fineract.infrastructure.paymentchannel.data;

import java.io.Serializable;
import java.util.Date;

import org.apache.fineract.infrastructure.paymentchannel.domain.PaymentChannel;
import org.apache.fineract.infrastructure.paymentchannel.domain.PaymentChannelType;

public class PaymentChannelData implements Comparable<PaymentChannelData>, Serializable{
	private Long id;
	private String channelName;
	private String channelEndpointTag;
	/**
     * A value from {@link PaymentChannelType}.
     */
	private int channelType;
	private boolean isActive;
	private String phoneNumberDefaultRegion;
	private Date dateCreated;
	private Date lastModified;	
	
	public PaymentChannelData(PaymentChannel paymentChannel) {
		super();
		this.id = paymentChannel.getId();
		this.channelName = paymentChannel.getChannelName();
		this.channelEndpointTag = paymentChannel.getChannelEndpointTag();
		this.channelType = paymentChannel.getChannelType();
		this.isActive = paymentChannel.isActive();
		this.phoneNumberDefaultRegion = paymentChannel.getPhoneNumberDefaultRegion();
		this.dateCreated = paymentChannel.getDateCreated();
		this.lastModified = paymentChannel.getLastModified();
	}
	
	public PaymentChannelData() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	@Override
	public int compareTo(PaymentChannelData obj) {
		if (obj == null) { return -1; }

        return obj.id.compareTo(this.id);
	}
	
	
}
