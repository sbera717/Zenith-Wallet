package com.example.keycloak_user_creation.Controller;


import com.example.keycloak_user_creation.Service.KeyCloakToken;
import com.example.keycloak_user_creation.Service.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final KeyCloakToken keyCloakToken;

    private final KeycloakUserService keycloakUserService;

    @GetMapping("/getlogintoken")
    public ResponseEntity<String> getToken(@RequestParam String username,@RequestParam String password){
        String token = keyCloakToken.getAccessToken(username,password);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/getMail/{username}")
    public String getUserEmail(@PathVariable String username){
        return  keycloakUserService.getUserEmail(username);
    }
}
