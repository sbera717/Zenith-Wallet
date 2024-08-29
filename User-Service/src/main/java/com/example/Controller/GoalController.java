package com.example.Controller;


import com.example.DTO.UserGoalCreation;
import com.example.Models.Goal;
import com.example.Models.User;
import com.example.Repository.GoalRepository;
import com.example.Service.CronService;
import com.example.Service.GoalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goalUser")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @Autowired
    private CronService cronService;

    @PostMapping("/goal-creation")
    public Goal goalCreate(@RequestBody @Valid UserGoalCreation userGoalCreation) throws JsonProcessingException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");
        return  goalService.createGoal(userGoalCreation,username);
    }

    @GetMapping("/goal-detail")
    public Goal goalDetails(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");
        return goalService.getGoal(username);
    }

    @GetMapping("/goalTrigger")
    public String goalTri() throws JsonProcessingException, InterruptedException {
        cronService.returnMoneyToUserWithCredit();
        return "OK";
    }

    @DeleteMapping("/goal-delete")
    public String goalDelete(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");
        goalService.deleteGoal(username);
        return "Your Goal will be deleted shortly and your goal balance will be transferred to main wallet with interest";
    }

}
