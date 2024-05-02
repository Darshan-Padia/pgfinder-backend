package com.pgfinder.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenRequest {
    private String token;
    private String email;
    private String role;
    // private String username;
    
}
