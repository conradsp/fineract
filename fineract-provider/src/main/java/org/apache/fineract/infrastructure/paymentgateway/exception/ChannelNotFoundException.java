package org.apache.fineract.infrastructure.paymentgateway.exception;

public class ChannelNotFoundException extends Exception {
	public ChannelNotFoundException(String message) {
		super(message);
	}
}
