package com.nd2k.authenticationapi.service;

import com.nd2k.authenticationapi.model.auth.Role;
import com.nd2k.authenticationapi.model.auth.User;
import com.nd2k.authenticationapi.model.dto.RegisterDto;
import com.nd2k.authenticationapi.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean registerNewUser(RegisterDto registerDto) {
        //TODO:Validate user input
        Set<Role> role = new HashSet<>();
        role.add(Role.ROLE_USER);
        User newUser = User.builder()
                .email(registerDto.getEmail())
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(role)
                .build();
        authRepository.save(newUser);
        return true;
    }

}
