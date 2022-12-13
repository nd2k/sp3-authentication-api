package com.nd2k.authenticationapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {

    private String userId;
    private String username;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private String accessToken;
    private String refreshToken;
}
