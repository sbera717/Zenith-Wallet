package com.example.DTO;

import com.example.Models.Goal;
import com.example.Models.GoalTxnStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UserGoalCreation {

    @NotBlank
    private String goal;

    private  String goalDescription;

    @NotNull
    private int duration;

    @NotNull
    private Double monthlyAmount;

    public Goal to(){
       return Goal.builder()
                .goal(this.goal)
                .goalDescription(this.goalDescription)
                .duration(this.duration)
               .monthlyAmount(this.monthlyAmount)
               .goalTxnStatus(GoalTxnStatus.PENDING)
                .build();
    }
}
