package org.apache.fineract.infrastructure.paymentgateway.service;

public interface PaymentGateway {
	
    public void processPayment(String payment);
}
