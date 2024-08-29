package com.example.keycloak_user_creation.DTO;

public record UserRegistrationRecord(String username, String email,String firstName,String lastName,String password) {
}
