package com.deepak.inventoryService.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            // Disable CSRF because this is a stateless REST API
            .csrf(csrf -> csrf.disable())

            // JWT based authentication - no session
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

            		
            	
                // ==========================================
                // ADMIN APIs
                // ==========================================
                .requestMatchers(
                    "/stock/create",
                    "/stock/update",
                    "/stock/getAllStocks",
                    "/stock/updatesku",
                    "/stock/updateskuByid",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()


                // ==========================================
                // CUSTOMER + ADMIN APIs
                // ==========================================
                .requestMatchers(
                    "/stock/getstock/**"
                ).hasAnyRole("CUSTOMER", "ADMIN")


                // ==========================================
                // INTERNAL / AUTHENTICATED APIs
                // ==========================================
                .requestMatchers(
                    "/stock/reduce/**",
                    "/stock/reducestock/**",
                    "/stock/getInventories"
                ).authenticated()


                // ==========================================
                // EVERYTHING ELSE
                // ==========================================
                .anyRequest().authenticated()
            )

            // JWT filter
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}