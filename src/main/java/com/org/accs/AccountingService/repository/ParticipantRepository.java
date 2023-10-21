package com.org.accs.AccountingService.repository;

import com.org.accs.AccountingService.persistance.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    Optional<Participant> findByPaymentInfo(String paymentInfo);
}
