package com.org.accs.messaging;

import com.org.accs.integration.MessageHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.org.ma.utils.Constants.PAYMENT_CHANNEL;

@Component
@RequiredArgsConstructor
public class MessageRouting extends RouteBuilder {

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String brokerAddress;

    private MessageHandler messageHandler;

    @Override
    public void configure() {
        from("kafka:%s?brokers=%s".formatted(PAYMENT_CHANNEL, brokerAddress))
                .routeId("payment-service")
                .process(messageHandler);
    }
}
