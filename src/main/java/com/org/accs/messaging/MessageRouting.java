package com.org.accs.messaging;

import com.org.accs.integration.MessageHandler;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static com.org.ma.utils.Constants.PAYMENT_CHANNEL;

@Component
@AllArgsConstructor
public class MessageRouting extends RouteBuilder {

    private MessageHandler messageHandler;

    @Override
    public void configure() {
        from("kafka:%s?brokers=172.18.0.1:9092".formatted(PAYMENT_CHANNEL))
                .routeId("payment-service")
                .process(messageHandler);
    }
}
