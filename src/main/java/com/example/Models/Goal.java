package com.example.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int goalId;

    @Column(unique = true,nullable = false)
    private  String username;

    private String goal;

    private String goalDescription;

    private LocalDate matureDate;

    private int duration;

    private int monthsRemaining;

    private Double monthlyAmount;

    private LocalDate lastAttemptedDate;

    @Enumerated (value = EnumType.STRING)
    private GoalTxnStatus goalTxnStatus;




}
