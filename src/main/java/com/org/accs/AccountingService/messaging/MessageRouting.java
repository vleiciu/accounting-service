package com.org.accs.AccountingService.messaging;

import com.org.accs.AccountingService.integration.MessageHandler;
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
        from("kafka:%s".formatted(PAYMENT_CHANNEL))
                .routeId("account-service")
                .process(messageHandler);
    }
}
