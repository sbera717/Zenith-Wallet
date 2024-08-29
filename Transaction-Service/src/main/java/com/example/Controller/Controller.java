package com.example.Controller;

import com.example.DTO.CreateTransactionRequest;
import com.example.Models.Transaction;
import com.example.Service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class Controller {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/initiate")
    public String initTxn(@Valid @RequestBody CreateTransactionRequest createTransactionRequest) throws JsonProcessingException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");

         return transactionService.initiateTxn(username,createTransactionRequest);

    }
    @GetMapping("/getAllTransaction")
    public List<Transaction> getAllTransaction(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");
        return transactionService.getAll(username);

    }
}
