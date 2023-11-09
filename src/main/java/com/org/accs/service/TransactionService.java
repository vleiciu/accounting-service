package com.org.accs.service;

import com.org.accs.persistance.Transaction;
import com.org.accs.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository repository;

    public void savePayment(Transaction transaction) {
        repository.save(transaction);
    }
}
