package com.alwozniak.form3.repository;

import com.alwozniak.form3.domain.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {

    @Query("SELECT ft from FinancialTransaction ft WHERE ft.transactionType = 'PAYMENT'")
    List<FinancialTransaction> findAllPayments();
}
