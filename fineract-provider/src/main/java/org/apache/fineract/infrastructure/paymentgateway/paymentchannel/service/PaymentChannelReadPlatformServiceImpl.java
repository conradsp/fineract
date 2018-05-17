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

package org.apache.fineract.infrastructure.paymentgateway.paymentchannel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.data.PaymentChannelData;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannel;
import org.apache.fineract.infrastructure.paymentgateway.paymentchannel.domain.PaymentChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentChannelReadPlatformServiceImpl implements PaymentChannelReadPlatformService{
	
	private final PaymentChannelRepository paymentChannelRepository;
	private final RoutingDataSource dataSource;
	
	@Autowired
	public PaymentChannelReadPlatformServiceImpl(final RoutingDataSource dataSource,
												 PaymentChannelRepository paymentChannelRepository) {
		this.dataSource = dataSource;
		this.paymentChannelRepository = paymentChannelRepository;
	}

	@Override
	public Collection<PaymentChannelData> retrieveAllPaymentChannelData() {
		List<PaymentChannel> paymentChannels = paymentChannelRepository.findAll();
		
		List<PaymentChannelData>  paymentChannelDataList = new ArrayList<>();
		for (PaymentChannel paymentChannel : paymentChannels) {
			paymentChannelDataList.add(new PaymentChannelData(paymentChannel));
		}
		return paymentChannelDataList;
	}

	@Override
	public PaymentChannelData retrievePaymentChannelDataById(Long id) {
		PaymentChannel paymentChannel = paymentChannelRepository.findOne(id);
		return new PaymentChannelData(paymentChannel);
	}

	@Override
	public PaymentChannelData findByChannelName(String channelName) {
		PaymentChannel paymentChannel = paymentChannelRepository.findByChannelName(channelName);
		return new PaymentChannelData(paymentChannel);
	}

}
