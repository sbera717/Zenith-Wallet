package com.example.Repository;

import com.example.Models.Transaction;
import com.example.Models.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface TxnRepository extends JpaRepository<Transaction,Integer> {

    Transaction findByExternalTxnId(String txnId);

    @Modifying
    @Query("update Transaction t set t.transactionStatus = ?2 where t.externalTxnId = ?1")
    void updateTxn(String txnId , TransactionStatus transactionStatus);


    @Query("select t from Transaction t where t.sender = ?1")
    List<Transaction> getAllTransaction(String username);

}
