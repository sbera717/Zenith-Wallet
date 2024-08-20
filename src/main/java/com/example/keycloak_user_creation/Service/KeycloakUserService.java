package com.example.keycloak_user_creation.Service;

import com.example.keycloak_user_creation.DTO.UserRegistrationRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakUserService {

        UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord);
        UserRepresentation getUserById(String userId);
        String getUserEmail(String username);
        String getUserByUsername(String username);
        void deleteUserById(String username);
        void emailVerification(String userId);
        UserResource getUserResource(String userId);
        void updatePassword(String userId);
    }

