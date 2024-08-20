package com.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TxnGoal {

    private String username;

    private Double amount;
}
