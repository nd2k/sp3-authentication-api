package com.nd2k.authenticationapi.service;

import com.nd2k.authenticationapi.model.auth.RefreshToken;
import com.nd2k.authenticationapi.model.auth.Role;
import com.nd2k.authenticationapi.model.auth.TokenDto;
import com.nd2k.authenticationapi.model.auth.User;
import com.nd2k.authenticationapi.model.dto.LoginDto;
import com.nd2k.authenticationapi.model.dto.RegisterDto;
import com.nd2k.authenticationapi.model.dto.AuthResponseDto;
import com.nd2k.authenticationapi.repository.AuthRepository;
import com.nd2k.authenticationapi.repository.RefreshTokenRepository;
import com.nd2k.authenticationapi.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final AuthRepository authRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final DaoAuthenticationProvider authenticationProvider;
    private final JwtUtils jwtUtils;
    private static final String TOKEN_INVALID = "Invalid token";

    public AuthResponseDto registerNewUser(RegisterDto registerDto) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));
        User newUser = User.builder()
                .email(registerDto.getEmail())
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .authorities(authorities)
                .build();
        RefreshToken refreshToken = saveRefreshToken(newUser);
        String accessToken = jwtUtils.generateAccessToken(newUser);
        String refreshTokenString = jwtUtils.generateRefreshToken(newUser, refreshToken);
        authRepository.save(newUser);
        return AuthResponseDto.builder()
                .userId(newUser.getId())
                .email(newUser.getEmail())
                .username(newUser.getUsername())
                .authorities(newUser.getAuthorities())
                .accessToken(accessToken)
                .refreshToken(refreshTokenString)
                .build();
    }
    public AuthResponseDto loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        RefreshToken refreshToken = saveRefreshToken(user);
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshTokenString = jwtUtils.generateRefreshToken(user, refreshToken);
        return AuthResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .authorities(user.getAuthorities())
                .accessToken(accessToken)
                .refreshToken(refreshTokenString)
                .build();
    }
    public void logoutUser(TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();
        String tokenId = jwtUtils.getTokenIdFromRefreshToken(refreshToken);
        if (jwtUtils.validateRefreshToken(refreshToken) &&
        refreshTokenRepository.existsById(tokenId)) {
            refreshTokenRepository.deleteById(tokenId);
        }
        throw new BadCredentialsException(TOKEN_INVALID);
    }
    public TokenDto generateAccessToken(TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();
        if (jwtUtils.validateRefreshToken(refreshToken) &&
                refreshTokenRepository.existsById(jwtUtils.getTokenIdFromRefreshToken(refreshToken))) {
            Optional<User> user = authRepository.findById(jwtUtils.getUserIdFromRefreshToken(refreshToken));
            String accessToken;
            if (user.isPresent()) {
                accessToken = jwtUtils.generateAccessToken(user.get());
                return TokenDto.builder()
                        .userId(user.get().getId())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        throw new BadCredentialsException(TOKEN_INVALID);
    }
    public TokenDto generateRefreshToken(TokenDto tokenDto) {
        String refreshTokenString = tokenDto.getRefreshToken();
        if (jwtUtils.validateRefreshToken(refreshTokenString) &&
                refreshTokenRepository.existsById(jwtUtils.getTokenIdFromRefreshToken(refreshTokenString))) {
            refreshTokenRepository.deleteById(jwtUtils.getTokenIdFromRefreshToken(refreshTokenString));
            Optional<User> user = authRepository.findById(jwtUtils.getUserIdFromRefreshToken(refreshTokenString));
            String newAccessToken;
            String newRefreshToken;
            if (user.isPresent()) {
                RefreshToken refreshToken = saveRefreshToken(user.get());
                newAccessToken = jwtUtils.generateAccessToken(user.get());
                newRefreshToken = jwtUtils.generateRefreshToken(user.get(), refreshToken);
                return TokenDto.builder()
                        .userId(user.get().getId())
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
            }
        }
        throw new BadCredentialsException(TOKEN_INVALID);
    }
    private RefreshToken saveRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user).build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
}
