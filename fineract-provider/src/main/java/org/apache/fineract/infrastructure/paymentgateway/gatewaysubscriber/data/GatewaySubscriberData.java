package org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.data;

import org.apache.fineract.infrastructure.paymentgateway.gatewaysubscriber.domain.GatewaySubscriber;
import org.apache.fineract.infrastructure.paymentgateway.payment.types.PaymentEntity;

public class GatewaySubscriberData {
	private Long id;
	private Long clientId;
	private Long entityId;
	/**
     * A value from {@link PaymentEntity}.
     */
	private PaymentEntity paymentEntity;
	private String paymentRef;
	
	public GatewaySubscriberData(GatewaySubscriber gatewaySubscriber) {
		super();
		this.id= gatewaySubscriber.getId();
		this.clientId= gatewaySubscriber.getClientId();
		this.entityId= gatewaySubscriber.getEntityId();
		this.paymentEntity= PaymentEntity.fromInt(gatewaySubscriber.getPaymentEntity());
		this.paymentRef= gatewaySubscriber.getPaymentRef();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public PaymentEntity getPaymentEntity() {
		return paymentEntity;
	}
	public void setPaymentEntity(PaymentEntity paymentEntity) {
		this.paymentEntity = paymentEntity;
	}
	public String getPaymentRef() {
		return paymentRef;
	}
	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}
}
