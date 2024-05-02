package com.pgfinder.dto;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDto {
    private String role;
    private String accessToken;
    private String email;
    private String tokenType  = "Bearer";
    // private String token;



    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthResponseDto(String role2, String accessToken2) {
        this.role = role2;
        this.accessToken = accessToken2;
    }

    public AuthResponseDto(String role2, String accessToken2, String email2) {
        this.role = role2;
        this.accessToken = accessToken2;
        this.email = email2;
    }

    // public AuthResponseDto(String token2, String refreshTokenString) {
    //     this.accessToken = token2;
    //     // this.token = refreshTokenString;
    // }

    // public AuthResponseDto(String rolee, String token2, String refreshTokenString) {
    //     this.role = rolee;
    //     this.accessToken = token2;
    //     this.token = refreshTokenString;
    // }
    
}
