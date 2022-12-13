package com.nd2k.authenticationapi.service;

import com.nd2k.authenticationapi.model.auth.Role;
import com.nd2k.authenticationapi.model.auth.User;
import com.nd2k.authenticationapi.model.dto.LoginDto;
import com.nd2k.authenticationapi.model.dto.RegisterDto;
import com.nd2k.authenticationapi.model.dto.ResponseDto;
import com.nd2k.authenticationapi.repository.AuthRepository;
import com.nd2k.authenticationapi.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final DaoAuthenticationProvider authenticationProvider;
    private final JwtUtils jwtUtils;

    public boolean registerNewUser(RegisterDto registerDto) {
        //TODO:Validate user input
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));
        User newUser = User.builder()
                .email(registerDto.getEmail())
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .authorities(authorities)
                .build();
        authRepository.save(newUser);
        return true;
    }

    public ResponseDto loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        return ResponseDto.builder()
                .email(authentication.getName())
                .authorities(authentication.getAuthorities())
                .jwtToken(jwtToken)
                .build();
    }
}
