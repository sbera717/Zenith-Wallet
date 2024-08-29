package com.example.Controller;

import com.example.DAO.ReturnUserDetail;
import com.example.DTO.UserCreateRequest;
import com.example.Models.User;
import com.example.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/registration")
    public ReturnUserDetail createUser(@RequestBody @Valid UserCreateRequest userCreate) throws JsonProcessingException {
        return  userService.create(userCreate);

    }
    @GetMapping("/getDetails")
    public ReturnUserDetail getUser(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");
        return userService.retriveUser(username);
    }

    @GetMapping("/txn/{mobileNo}")
    public User getTxnUser(@PathVariable ("mobileNo") String mobileNo){
        return (User) userService.returnUser(mobileNo);
    }

    @DeleteMapping("/deleteAccount")
    public String deleteUser(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaim("preferred_username");
        userService.deleteAUser(username);
        return  "Your account will be deleted shortly you will get email shortly regarding balance transfer to bank";
    }





}
