package org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GatewaySubscriberRepository
		extends JpaRepository<GatewaySubscriber, Long>, JpaSpecificationExecutor<GatewaySubscriber> {
	public GatewaySubscriber findByClientIdAndPaymentRef(Long clientId, String entityId);
}
