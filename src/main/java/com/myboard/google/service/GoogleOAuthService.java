package com.myboard.google.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myboard.google.dto.GoogleInfResponse;
import com.myboard.google.dto.GoogleRequest;
import com.myboard.google.dto.GoogleResponse;

@Service
public class GoogleOAuthService {
    
    public ResponseEntity<GoogleInfResponse> getGoogleUserInfo(String authCode, String type, 
            String id, String secret, String server) {

        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(id)
                .clientSecret(secret)
                .code(authCode)
                .redirectUri(server + "/"+type+"/google/access")
                .grantType("authorization_code").build();
        
        // Request a Google access token
        ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                googleOAuthRequestParam, GoogleResponse.class);
        
        String jwtToken=resultEntity.getBody().getId_token();
        
        Map<String, String> map=new HashMap<>();
        map.put("id_token",jwtToken);
        
        // Request a Google User Info with access token
        ResponseEntity<GoogleInfResponse> resultEntity2 = 
                restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo", map, GoogleInfResponse.class);
    
        return resultEntity2;
    }
}
