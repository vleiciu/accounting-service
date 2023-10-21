package com.org.accs.AccountingService.persistance;

import com.org.accs.AccountingService.enums.Result;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACTIONS")
@Builder
@Data
public class Transaction {

    @Id
    @Column(name = "TRANSACTION_ID")
    private String transactionId;

    @Column(name = "SENDER_ID")
    private String senderId;

    @Column(name = "RECEIVER_ID")
    private String receiverId;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "CORRELATION_ID")
    private String correlationId;

    @Column(name = "ISSUED_AT")
    private LocalDateTime issuedAt;

    @Enumerated(EnumType.STRING)
    private Result result;
}
