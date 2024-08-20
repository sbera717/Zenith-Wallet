package com.example.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GoalWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int goalWalletId;
    private Double balance;

    @Column(unique = true,nullable = false)
    private String username;

    @CreationTimestamp
    private Date createdOn;
    @UpdateTimestamp
    private LocalDate updatedOn;
}
