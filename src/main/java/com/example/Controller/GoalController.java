package com.example.Controller;


import com.example.Repository.GoalWalletRepository;
import com.example.Service.GoalWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goalWallet")
public class GoalController {
    @Autowired
    private GoalWalletService goalWalletService;

    @GetMapping("/goalBalance")
    public double getGoalBalance(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");
        return goalWalletService.getGoalBalance(username);
    }
}
