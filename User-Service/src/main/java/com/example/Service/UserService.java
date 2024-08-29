package com.example.Service;

import com.example.DAO.ReturnUserDetail;
import com.example.DTO.UserCreateRequest;
import com.example.Models.User;
import com.example.Repository.UserRepository;
import com.example.Utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GoalService goalService;
    public ReturnUserDetail create(UserCreateRequest userCreateRequest) throws JsonProcessingException {
        User user = userCreateRequest.to();
        JSONObject event = objectMapper.convertValue(user, JSONObject.class);
        String msg = objectMapper.writeValueAsString(event);
        kafkaTemplate.send("keycloak",msg); // communicate a keycloak-service
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        kafkaTemplate.send(Constants.User_Topic,msg); // communicate a wallet-service
        ReturnUserDetail returnUserDetail = new ReturnUserDetail(user.getName(), user.getEmail(),user.getMobile(),user.getCreatedOn(),user.getUpdatedOn());
        return  returnUserDetail;

    }
    public ReturnUserDetail retriveUser(String username){
        User user = userRepository.findByMobile(username);
        return new ReturnUserDetail(user.getName(), user.getEmail(),user.getMobile(),user.getCreatedOn(),user.getUpdatedOn());
    }

    public User returnUser(String mobile){
        return userRepository.findByMobile(mobile);
    }

    public void deleteAUser(String username){
        User user = userRepository.findByMobile(username);
        userRepository.deleteById(user.getUserId());
        goalService.deleteGoal(username);
        kafkaTemplate.send(Constants.User_Delete_Account,username);

    }






}
