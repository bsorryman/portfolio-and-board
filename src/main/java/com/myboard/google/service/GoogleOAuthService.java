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
    
    public ResponseEntity<GoogleInfResponse> getGoogleUserInfo(String code, String type, 
            String id, String secret, String server) {
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .code(code)
                .clientId(id)
                .clientSecret(secret)
                .redirectUri(server + "/"+type+"/google/access")
                .grantType("authorization_code").build();
        
        // Request a Google access token
        ResponseEntity<GoogleResponse> tokenEntity = 
        		restTemplate.postForEntity("https://oauth2.googleapis.com/token", googleOAuthRequestParam, GoogleResponse.class);
        
        String idToken = tokenEntity.getBody().getId_token();
        
        Map<String, String> map = new HashMap<>();
        map.put("id_token",idToken);
        
        // Request a Google User Info with access token
        ResponseEntity<GoogleInfResponse> infoEntity = 
                restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo", map, GoogleInfResponse.class);
    
        return infoEntity;
    }
}
