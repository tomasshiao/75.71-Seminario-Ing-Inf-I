package com.example.trackNGO.Repositories

import com.example.trackNGO.Model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

import javax.persistence.Id

interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findTransactionById(@Param("txnId") Id txnId)
}