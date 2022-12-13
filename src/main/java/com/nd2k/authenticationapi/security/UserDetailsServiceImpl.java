package com.nd2k.authenticationapi.security;

import com.nd2k.authenticationapi.model.auth.User;
import com.nd2k.authenticationapi.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthRepository authRepository;
    @Transactional
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return authRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
