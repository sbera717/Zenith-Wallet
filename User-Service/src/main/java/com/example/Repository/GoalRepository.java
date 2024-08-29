package com.example.Repository;

import com.example.DTO.Interest;
import com.example.Models.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal,Integer> {


    Goal findByUsername(String username);


    @Query("select new com.example.DTO.Interest(g.username,g.monthlyAmount) from Goal g where g.duration > 0")
    List<Interest> deductAmountPerMonthOn1st();


    @Query("select new com.example.DTO.Interest(g.username,g.monthlyAmount) from Goal g where g.duration > 0 AND g.goalTxnStatus = 'FAILED' ")
    List<Interest> deductAmountPerMonthOn5th();


    @Query("select g.username from Goal g where g.matureDate = ?1 ")
    List<String> findUserToCreditAmountWithInterest(LocalDate localDate);

}
