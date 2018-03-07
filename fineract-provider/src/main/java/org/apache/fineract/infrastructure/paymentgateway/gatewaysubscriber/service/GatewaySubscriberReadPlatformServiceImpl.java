package org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.data.GatewaySubscriberData;
import org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.domain.GatewaySubscriber;
import org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.domain.GatewaySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatewaySubscriberReadPlatformServiceImpl implements GatewaySubscriberReadPlatformService {

	private final static Logger logger = LoggerFactory.getLogger(GatewaySubscriberReadPlatformServiceImpl.class);

	private final GatewaySubscriberRepository gatewaySubscriberRepository;

	public GatewaySubscriberReadPlatformServiceImpl(GatewaySubscriberRepository gatewaySubscriberRepository) {
		super();
		this.gatewaySubscriberRepository = gatewaySubscriberRepository;
	}

	@Override
	public Collection<GatewaySubscriberData> retrieveAllGatewaySubscriberData() {
		List<GatewaySubscriber> gatewaySubscribers = gatewaySubscriberRepository.findAll();

		List<GatewaySubscriberData> gatewaySubscriberDataList = new ArrayList<>();
		for (GatewaySubscriber gatewaySubscriber : gatewaySubscribers) {
			gatewaySubscriberDataList.add(new GatewaySubscriberData(gatewaySubscriber));
		}
		return gatewaySubscriberDataList;
	}

	@Override
	public GatewaySubscriberData findById(Long resourceId) {
		GatewaySubscriber gatewaySubscriber = gatewaySubscriberRepository.findOne(resourceId);
		return new GatewaySubscriberData(gatewaySubscriber);
	}

	@Override
	public GatewaySubscriberData findByClientIdAndPaymentRef(Long clientId, String entityId) {
		GatewaySubscriber gatewaySubscriber = gatewaySubscriberRepository.findByClientIdAndPaymentRef(clientId,
				entityId);
		return new GatewaySubscriberData(gatewaySubscriber);
	}

}
