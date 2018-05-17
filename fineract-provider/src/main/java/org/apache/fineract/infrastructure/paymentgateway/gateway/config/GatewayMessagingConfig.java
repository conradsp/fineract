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
package org.apache.fineract.infrastructure.paymentgateway.gateway.config;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.fineract.infrastructure.core.boot.db.TenantDataSourcePortFixService;
import org.apache.fineract.infrastructure.paymentgateway.gateway.service.GatewayEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jms.connection.CachingConnectionFactory;
import javax.jms.*;
import org.springframework.stereotype.Component;

@Component
public class GatewayMessagingConfig {

    private Environment env;
    private GatewayEventListener gatewayEventListener;
    private String channelName;

    @Bean
    public Logger loggerBean() { return LoggerFactory.getLogger(TenantDataSourcePortFixService.class); }

    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";

    private String requestQueueName;
    private String responseQueueName;
    private CachingConnectionFactory connectionFactory;
    private ActiveMQConnectionFactory amqConnectionFactory;

    @Autowired
    public GatewayMessagingConfig(Environment env, GatewayEventListener gatewayEventListener) {
        this.env = env;
        this.gatewayEventListener = gatewayEventListener;

        amqConnectionFactory = new ActiveMQConnectionFactory();
        try {
            amqConnectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
            amqConnectionFactory.setUserName("admin");
            amqConnectionFactory.setPassword("admin");
        } catch(Exception e) {
            amqConnectionFactory.setBrokerURL(this.env.getProperty("brokerUrl"));
        }

        connectionFactory = new CachingConnectionFactory(amqConnectionFactory);
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void connectQueue() {
        setRequestQueueName(this.channelName+".request");
        setResponseQueueName(this.channelName+".response");

        // Establish a connection for the producer.
        try {

            // Establish a connection for the consumer.
            // Note: Consumers should not use PooledConnectionFactory.
            final Connection consumerConnection = amqConnectionFactory.createConnection();
            consumerConnection.start();

            // Create a session.
            final Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            final Destination consumerDestination = consumerSession.createQueue(getRequestQueueName());

            // Create a message consumer from the session to the queue.
            final MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);
            consumer.setMessageListener(gatewayEventListener);

        } catch(Exception E) {

        }

    }

    public void sendMessage(String queueName, String message) {
        try {
            final Connection producerConnection = connectionFactory.createConnection();
            producerConnection.start();

            // Create a session.
            final Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            final Destination producerDestination = producerSession.createQueue(queueName);

            // Create a producer from the session to the queue.
            final MessageProducer producer = producerSession.createProducer(producerDestination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a message.
            final TextMessage producerMessage = producerSession.createTextMessage(message);

            // Send the message.
            producer.send(producerMessage);

            // Clean up the producer.
            producer.close();
            producerSession.close();
            producerConnection.close();
        } catch (Exception E) {

        }
    }


    public String getRequestQueueName() {
        return requestQueueName;
    }

    public void setRequestQueueName(String requestQueueName) {
        this.requestQueueName = requestQueueName;
    }

    public String getResponseQueueName() {
        return responseQueueName;
    }

    public void setResponseQueueName(String responseQueueName) {
        this.responseQueueName = responseQueueName;
    }
}
