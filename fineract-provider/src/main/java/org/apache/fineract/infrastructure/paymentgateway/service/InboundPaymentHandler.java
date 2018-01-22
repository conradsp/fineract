package org.apache.fineract.infrastructure.paymentgateway.service;

import org.apache.fineract.infrastructure.paymentgateway.util.PaymentGatewayConstants;
import org.springframework.messaging.Message;

public class InboundPaymentHandler {

    public void handlePayment(Message<String> message){
        Object channelName = message.getHeaders().get(PaymentGatewayConstants.CHANNEL_NAME_HEADER);
        System.out.println("Printing the message:");
        System.out.println(message);
    }
}
