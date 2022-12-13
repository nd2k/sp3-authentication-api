package com.nd2k.authenticationapi.controller;

import com.nd2k.authenticationapi.model.auth.TokenDto;
import com.nd2k.authenticationapi.model.dto.LoginDto;
import com.nd2k.authenticationapi.model.dto.RegisterDto;
import com.nd2k.authenticationapi.model.dto.AuthResponseDto;
import com.nd2k.authenticationapi.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserAuthService userAuthService;
    @SuppressWarnings("unused")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerNewUser(@Valid @RequestBody RegisterDto registerDto) {
        return new ResponseEntity<>(userAuthService.registerNewUser(registerDto), HttpStatus.CREATED);
    }
    @SuppressWarnings("unused")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(userAuthService.loginUser(loginDto), HttpStatus.OK);
    }
    @SuppressWarnings("unused")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenDto tokenDto) {
        userAuthService.logoutUser(tokenDto);
        return new ResponseEntity<>("User successfully logout", HttpStatus.OK);
    }
    @SuppressWarnings("unused")
    @PostMapping("/access-token")
    public ResponseEntity<TokenDto> accessToken(@RequestBody TokenDto tokenDto) {
        TokenDto renewedTokenDto = userAuthService.generateAccessToken(tokenDto);
        return new ResponseEntity<>(renewedTokenDto, HttpStatus.OK);
    }
    @SuppressWarnings("unused")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody TokenDto tokenDto) {
        TokenDto renewedTokenDto = userAuthService.generateRefreshToken(tokenDto);
        return new ResponseEntity<>(renewedTokenDto, HttpStatus.OK);
    }
}
