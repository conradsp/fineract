package org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.service;

import java.util.Collection;

import org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.data.GatewaySubscriberData;

public interface GatewaySubscriberReadPlatformService {
	
    Collection<GatewaySubscriberData> retrieveAllGatewaySubscriberData();
	
    GatewaySubscriberData findById(Long id);
	
    GatewaySubscriberData findByClientIdAndPaymentRef(Long clientId, String entityId);
}
