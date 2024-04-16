package com.myboard.google.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleRequest {
    private String code;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantType;
}
