package com.org.accs.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "PARTICIPANTS")
@Builder
@Data
public class Participant {

    @Id
    @Column(name = "PARTICIPANT_ID")
    private String participantId;

    @Column(name = "AVAILABLE")
    private Double available;

    @Column(name = "CREDIT")
    private Double credit;
}
