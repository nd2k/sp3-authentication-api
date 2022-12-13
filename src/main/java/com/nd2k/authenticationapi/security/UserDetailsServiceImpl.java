package com.nd2k.authenticationapi.security;

import com.nd2k.authenticationapi.model.auth.User;
import com.nd2k.authenticationapi.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = authRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(existingUser.getEmail())
                .authorities(existingUser.getAuthorities())
                .password(existingUser.getPassword())
                .build();
    }
}
