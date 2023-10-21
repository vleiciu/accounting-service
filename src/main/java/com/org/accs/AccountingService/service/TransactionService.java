package com.org.accs.AccountingService.service;

import com.org.accs.AccountingService.persistance.Transaction;
import com.org.accs.AccountingService.repository.TransactionRepository;
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
