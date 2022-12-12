package com.nd2k.authenticationapi.controller;

import com.nd2k.authenticationapi.model.dto.RegisterDto;
import com.nd2k.authenticationapi.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Boolean.TRUE;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody RegisterDto registerDto) {
        if (TRUE.equals(userAuthService.registerNewUser(registerDto))) {
            return new ResponseEntity<>("New user has been created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("An technical issue happened", HttpStatus.BAD_REQUEST);
        }
    }
}
