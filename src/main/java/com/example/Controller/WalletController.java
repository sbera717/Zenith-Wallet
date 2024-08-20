package com.example.Controller;

import com.example.Service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;


    @GetMapping("/balance")
    public double balance(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");
        return walletService.getBalance(username);
    }
    @PutMapping("/addMoney")
    public String addMoney(@RequestParam int money){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");
        walletService.addMoney(username,(double)money);
        return money + " added Successfully";

    }

}
