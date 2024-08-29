package com.example.Service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeyCloakCommunication {

    @Autowired
    private RestTemplate restTemplate;

    public String getEmail(String username){
         //Creating authorization header for txn service
        String plainCreds = "1111111111:12345";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes,false);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "http://login:80/login/getMail/" + username;
        String email = null;
        try{
            email = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return  email;

    }

}
