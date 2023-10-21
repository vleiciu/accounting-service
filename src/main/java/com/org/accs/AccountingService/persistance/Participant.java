package com.org.accs.AccountingService.persistance;

import com.org.accs.AccountingService.enums.ParticipantType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "PARTICIPANTS")
@Builder
@Data
public class Participant {

    @Id
    @Column(name = "PARTICIPANT_ID")
    private Integer participantId;

    @Column(name = "PAYMENT_INFO")
    private String paymentInfo;

    @Column(name = "AVAILABLE")
    private Double available;

    @Column(name = "CREDIT")
    private Double credit;

    @Enumerated(EnumType.STRING)
    private ParticipantType type;
}
