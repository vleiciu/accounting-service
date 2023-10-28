package com.org.accs.AccountingService.integration;

import com.org.accs.AccountingService.enums.Result;
import com.org.accs.AccountingService.persistance.Participant;
import com.org.accs.AccountingService.persistance.Transaction;
import com.org.accs.AccountingService.service.ParticipantService;
import com.org.accs.AccountingService.service.TransactionService;
import com.org.ma.enums.MessageType;
import com.org.ma.enums.Subject;
import com.org.ma.model.Payment;
import com.org.ma.model.PaymentUpdate;
import com.org.ma.utils.Constants;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.org.ma.enums.Header.RESPONSE;
import static com.org.ma.utils.Constants.MESSAGE_TYPE;
import static com.org.ma.utils.Constants.SUBJECT;

@Component
@AllArgsConstructor
public class MessageHandler implements Processor {

    private ParticipantService participantService;

    private TransactionService transactionService;

    private KafkaProducer<String, Payment> producer;

    @Override
    public void process(Exchange exchange) {
        MessageType messageType = exchange.getIn().getHeader(Constants.MESSAGE_TYPE, MessageType.class);
        if (messageType.equals(MessageType.REGULAR)) {
            handleRegularRequest(exchange);
        } else if (messageType.equals(MessageType.INFO)) {
            handleInfoRequest(exchange);
        } else {
            handleCancelRequest(exchange);
        }
    }

    private void handleRegularRequest(Exchange exchange) {
        Payment payment = exchange.getMessage(Payment.class);
        Transaction transaction = Transaction.builder()
                .correlationId(payment.getCorrelationId())
                .amount(payment.getAmount())
                .receiverId(payment.getReceiverId())
                .senderId(payment.getSenderId())
                .build();
        transaction.setTransactionId(UUID.fromString(transaction.toString()).toString());
        boolean successfulTransaction = processPayment(transaction);
        ProducerRecord<String, Payment> record = new ProducerRecord<>(Constants.ORDER_CHANNEL, Constants.MESSAGE, payment);
        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.PAYMENT.name(), RESPONSE.name()).getBytes());
        if (successfulTransaction) {
            record.headers().add(MESSAGE_TYPE, MessageType.REGULAR.name().getBytes());
            transaction.setResult(Result.SUCCESS);
        } else {
            record.headers().add(MESSAGE_TYPE, MessageType.REJECT.name().getBytes());
            transaction.setResult(Result.FAIL);
        }
        transaction.setIssuedAt(LocalDateTime.now());
        transactionService.savePayment(transaction);
        producer.send(record);
    }

    private void handleCancelRequest(Exchange exchange) {
        Payment payment = exchange.getMessage(Payment.class);
        Participant senderParticipant = participantService.getByParticipantId(payment.getSenderId());
        Participant receiverParticipant = participantService.getByParticipantId(payment.getReceiverId());
        senderParticipant.setAvailable(senderParticipant.getAvailable() + payment.getAmount());
        receiverParticipant.setAvailable(receiverParticipant.getAvailable() - payment.getAmount());
        participantService.saveParticipant(receiverParticipant);
        participantService.saveParticipant(senderParticipant);
        Transaction transaction = Transaction.builder()
                .correlationId(payment.getCorrelationId())
                .amount(payment.getAmount())
                .receiverId(payment.getSenderId())
                .senderId(payment.getReceiverId())
                .result(Result.SUCCESS)
                .issuedAt(LocalDateTime.now())
                .build();
        transaction.setTransactionId(UUID.fromString(transaction.toString()).toString());
        ProducerRecord<String, Payment> record = new ProducerRecord<>(Constants.ORDER_CHANNEL, Constants.MESSAGE, payment);
        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.PAYMENT.name(), RESPONSE.name()).getBytes());
        record.headers().add(MESSAGE_TYPE, MessageType.CANCEL.name().getBytes());
        transactionService.savePayment(transaction);
        producer.send(record);
    }

    private void handleInfoRequest(Exchange exchange) {
        PaymentUpdate update = exchange.getMessage(PaymentUpdate.class);
        participantService.saveParticipant(Participant.builder()
                .participantId(update.getParticipantId())
                .available(update.getAvailable())
                .credit(update.getCredit())
                .build());
    }

    private boolean processPayment(Transaction transaction) {
        Participant senderParticipant = participantService.getByParticipantId(transaction.getSenderId());
        Participant receiverParticipant = participantService.getByParticipantId(transaction.getReceiverId());
        Double transactionAmount = transaction.getAmount();
        if ((senderParticipant.getAvailable() + senderParticipant.getCredit()) - transactionAmount > 0) {
            senderParticipant.setAvailable(senderParticipant.getAvailable() - transactionAmount);
            receiverParticipant.setAvailable(receiverParticipant.getAvailable() + transactionAmount);
            participantService.saveParticipant(receiverParticipant);
            participantService.saveParticipant(senderParticipant);
            return true;
        }
        return false;
    }
}
