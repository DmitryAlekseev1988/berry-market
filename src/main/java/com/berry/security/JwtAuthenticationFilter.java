package com.berry.security;

import java.io.IOException;
import java.util.Collections;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.berry.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        Long userId = null;
        String jwt = null;

        // достаем токен из заголовка Authorization
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                userId = jwtUtil.extractUserId(jwt);
            } catch (Exception e) {
                logger.error("Ошибка извлечения user id из JWT: " + e.getMessage());
            }
        }

        // если токен валидный, устанавливаем аутентификацию в контекст
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userDetailsService.loadUserById(userId);

            if (jwtUtil.validateToken(jwt, userId)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user.getId().toString(), null,
                                Collections.singletonList(new SimpleGrantedAuthority(
                                        "ROLE_" + user.getType().name())));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
