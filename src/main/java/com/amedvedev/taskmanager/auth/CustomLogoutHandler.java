package com.amedvedev.taskmanager.auth;

import com.amedvedev.taskmanager.config.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(cookie -> "JWT".equals(cookie.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        String token = cookie.getValue();
                        jwtService.blacklistToken(
                                token,
                                jwtService.extractClaim(token, Claims::getExpiration).toInstant()
                        );
                    });
        }
        SecurityContextHolder.clearContext();
    }
}
