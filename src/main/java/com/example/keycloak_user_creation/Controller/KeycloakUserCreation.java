package com.example.keycloak_user_creation.Controller;

import com.example.keycloak_user_creation.DTO.UserRegistrationRecord;
import com.example.keycloak_user_creation.Service.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class KeycloakUserCreation {

    private final KeycloakUserService keycloakUserService;
    @PostMapping
    public UserRegistrationRecord createUser(@RequestBody UserRegistrationRecord userRegistrationRecord) {

        return keycloakUserService.createUser(userRegistrationRecord);
    }

    @GetMapping
    public UserRepresentation getUser(Principal principal) {

        return keycloakUserService.getUserById(principal.getName());
    }

    @GetMapping("/{username}")
    public String  getUserByUsername(@PathVariable String username){
        return keycloakUserService.getUserByUsername(username);
    }

//    @GetMapping("/getMail/{username}")
//    public String getUserEmail(@PathVariable String username){
//        return  keycloakUserService.getUserEmail(username);
//    }

//    @DeleteMapping("/{userId}")
//    public void deleteUserById(@PathVariable String userId) {
//        keycloakUserService.deleteUserById(userId);
//    }


    @PutMapping("/{userId}/send-verify-email")
    public void sendVerificationEmail(@PathVariable String userId) {
        keycloakUserService.emailVerification(userId);
    }
    @PutMapping("/update-password")
    public void updatePassword(Principal principal) {
        keycloakUserService.updatePassword(principal.getName());
    }
}
