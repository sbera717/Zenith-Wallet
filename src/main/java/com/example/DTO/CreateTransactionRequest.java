package com.example.DTO;

import com.example.Models.Transaction;
import com.example.Models.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {


    //private String sender // from security context ;

    @NotBlank
    private  String receiver;

    @NotNull
    private double amount;

    private String purpose;


    public Transaction to(String sender){
        return Transaction.builder()
                .externalTxnId(UUID.randomUUID().toString())
                .sender(sender)
                .receiver(this.receiver)
                .amount(this.amount)
                .purpose(this.purpose)
                .transactionStatus(TransactionStatus.PENDING)
                .build();
    }


}
