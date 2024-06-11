package com.amedvedev.taskmanager.config;

import com.amedvedev.taskmanager.auth.CustomLogoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomLogoutHandler customLogoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer ->
                        customizer
                                .requestMatchers(
                                        "/auth/**",
                                        "/**.css",
                                        "/scripts/**",
                                        "/images/**",
                                        "/**.html",
                                        "/error").permitAll()
                                .anyRequest().authenticated()
                )
                .logout(
                        customizer -> customizer
                                .logoutUrl("/auth/logout")
                                .deleteCookies("JWT")
                                .addLogoutHandler(customLogoutHandler)
                                .logoutSuccessUrl("/auth/login")
                )
                .exceptionHandling(customizer ->
                        customizer.authenticationEntryPoint(
                                (request, response, authException) -> response.sendRedirect("/auth/login")
                        )
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
