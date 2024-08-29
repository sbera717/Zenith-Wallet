package com.example.keycloak_user_creation.Service;

import com.example.keycloak_user_creation.DTO.UserRegistrationRecord;
import com.example.keycloak_user_creation.Utils.Constants;
import jakarta.ws.rs.core.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
public class KeycloakUserServiceImpl implements KeycloakUserService,RoleService{

    @Value("${keycloak.realm}")
    private String realm;
    private final Keycloak keycloak;

    public KeycloakUserServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Autowired
    private JSONParser jsonParser;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @KafkaListener(topics = "keycloak",groupId = "keycloak_1",errorHandler = "kafkaHandler")
    public void receiveKafkaMsg(String msg) throws ParseException {
        JSONObject event = (JSONObject) jsonParser.parse(msg);
        String username = (String) event.get("mobile");
        String email = (String) event.get("email");
        String firstName = (String) event.get("name");
        String password = (String) event.get("password");
        System.out.println(username);
        System.out.println(email);
        System.out.println(firstName);
        System.out.println(password);

        UserRegistrationRecord userRegistrationRecord = new UserRegistrationRecord(username,email,firstName,"Bera",password);
        createUser(userRegistrationRecord);


    }
    @KafkaListener(topics = Constants.User_Delete_Account,groupId = "user_delete_keycloak1",errorHandler = "kafkaHandler")
    public void deleteUser(String username){
        deleteUserById(username);
    }

    @Override
    public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord) {

        UserRepresentation user=new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRegistrationRecord.username());
        user.setEmail(userRegistrationRecord.email());
        user.setFirstName(userRegistrationRecord.firstName());
        user.setLastName(userRegistrationRecord.lastName());
        user.setEmailVerified(true);

        CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
        credentialRepresentation.setValue(userRegistrationRecord.password());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> list = new ArrayList<>();
        list.add(credentialRepresentation);
        user.setCredentials(list);

        UsersResource usersResource = getUsersResource();

        Response response = usersResource.create(user);
        System.out.println(response.getStatus());

        if(Objects.equals(201,response.getStatus())){

            List<UserRepresentation> representationList = usersResource.searchByUsername(userRegistrationRecord.username(), true);
            if(!CollectionUtils.isEmpty(representationList)){
                UserRepresentation createdUser = representationList.get(0);
                String id = createdUser.getId();
                assignRole(id,"app_user");
            }
            kafkaTemplate.send(Constants.New_Account,userRegistrationRecord.email());
            return  userRegistrationRecord;
        }

        return null;
    }

    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(realm);
        return realm1.users();
    }

    @Override
    public UserRepresentation getUserById(String userId) {
        return  getUsersResource().get(userId).toRepresentation();
    }

    @Override
    public String getUserEmail(String username) {
        List<UserRepresentation>  users= getUsersResource().search(username);
        if(users.size() > 0){
            UserRepresentation user = users.get(0);
            return user.getEmail();
        }
        return null;
    }

    @Override
    public String getUserByUsername(String username) {
        List<UserRepresentation>  users= getUsersResource().search(username);
        if(users.size() > 0){
            UserRepresentation user = users.get(0);
            return user.getId();
        }
        return  null;
    }



    @Override
    public void deleteUserById(String username) {
        try{
            String userId = getUserByUsername(username);
            getUsersResource().delete(userId);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void emailVerification(String userId){

        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    public UserResource getUserResource(String userId){
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    @Override
    public void updatePassword(String userId) {

        UserResource userResource = getUserResource(userId);
        List<String> actions= new ArrayList<>();
        actions.add("UPDATE_PASSWORD");
        userResource.executeActionsEmail(actions);

    }

    @Override
    public void assignRole(String userId, String roleName) {
        UserResource userResource = getUserResource(userId);
        RolesResource rolesResource = getRolesResource();
        RoleRepresentation representation = rolesResource.get(roleName).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(representation));
    }

    private RolesResource getRolesResource(){
        return  keycloak.realm(realm).roles();
    }
}
