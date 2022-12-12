package com.nd2k.authenticationapi.model.dto;

import com.nd2k.authenticationapi.model.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {

    private String username;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private String jwtToken;
}
