package com.example.Repository;

import com.example.Models.GoalWallet;
import com.example.Models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;

public interface GoalWalletRepository extends JpaRepository<GoalWallet, Integer> {

    GoalWallet findByUsername(String username);


    @Query("select g.balance from GoalWallet g where g.username = ?1")
    Double findGoalBalance(String username);


}
