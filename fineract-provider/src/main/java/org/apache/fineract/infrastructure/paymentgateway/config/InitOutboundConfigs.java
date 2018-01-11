package org.apache.fineract.infrastructure.paymentgateway.config;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.apache.fineract.infrastructure.paymentchannel.data.PaymentChannelData;
import org.apache.fineract.infrastructure.paymentchannel.service.PaymentChannelReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class InitOutboundConfigs {
	@Autowired
	@Qualifier("paymentChannelResolver")
	private DynamicAMQChannelResolver amqChannelResolver;
	@Autowired
	private PaymentChannelReadPlatformService paymentChannelReadPlatformService;

	@PostConstruct
	public void init() {
		Collection<PaymentChannelData> paymentChannelList = paymentChannelReadPlatformService
				.retrieveAllPaymentChannelData();
		for (PaymentChannelData paymentChannelData : paymentChannelList) {
			amqChannelResolver.createPaymentChannel(paymentChannelData.getChannelName(),
					paymentChannelData.getChannelBrokerEndpoint());
		}
	}
}