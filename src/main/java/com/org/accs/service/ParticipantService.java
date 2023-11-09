package com.org.accs.service;

import com.org.accs.persistance.Participant;
import com.org.accs.repository.ParticipantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ParticipantService {

    private ParticipantRepository participantRepository;

    public Participant getByParticipantId(String participantId) {
        return participantRepository.findByParticipantId(participantId).get();
    }

    public void saveParticipant(Participant participant) {
        participantRepository.save(participant);
    }
}
