package com.nd2k.authenticationapi.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    private String userId;
    private String accessToken;
    private String refreshToken;
}
