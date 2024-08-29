package com.example.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String externalTxnId;

    private String sender;

    private String receiver;

    private  Double amount;

    private  String purpose;

    @Enumerated (value = EnumType.STRING)
    private TransactionStatus transactionStatus;

    @CreationTimestamp
    private Date transactionTime;

    @UpdateTimestamp
    private Date updatedOn;

}
