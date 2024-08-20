package com.example.keycloak_user_creation.Controller;

import com.example.keycloak_user_creation.Service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class KeyCloakRoleAssign {

    private final RoleService roleService;


    @PutMapping("/assign-role/user/{userId}")
    public ResponseEntity<?> assignRole(@PathVariable String userId, @RequestParam String roleName) {

        try{
            roleService.assignRole(userId, roleName);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
