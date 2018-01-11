package org.apache.fineract.infrastructure.paymentgateway.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class AMQHelper implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	public void sendMessage(String channelName, String message) {
		MessageChannel channel = applicationContext.getBean("routerChannel", MessageChannel.class);

		Message<String> payload = MessageBuilder.withPayload(message).setHeader("channel-name", channelName).build();

        channel.send(payload);
	}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
