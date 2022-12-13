package com.nd2k.authenticationapi.security;

import com.nd2k.authenticationapi.model.auth.User;
import com.nd2k.authenticationapi.repository.AuthRepository;
import com.nd2k.authenticationapi.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationJwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthRepository authRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> jwtToken = parseJwt(request);
            if (jwtToken.isPresent() && jwtUtils.validateAccessToken(jwtToken.get())) {
                String userId = jwtUtils.getUserIdFromAccessToken(jwtToken.get());
                Optional<User> user = authRepository.findById(userId);
                if (user.isPresent()) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user.get().getEmail(), null, user.get().getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> parseJwt(HttpServletRequest request) {
        String headerAuthJwt = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuthJwt) && headerAuthJwt.startsWith("Bearer ")) {
            return Optional.of(headerAuthJwt.substring(7));
        }
        return Optional.empty();
    }
}
