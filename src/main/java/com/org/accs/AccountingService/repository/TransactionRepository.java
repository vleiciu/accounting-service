package com.org.accs.AccountingService.repository;

import com.org.accs.AccountingService.persistance.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
