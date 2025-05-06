package com.busra.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{



        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_PATRON")
                        .requestMatchers("/api/books/**").hasAuthority("ROLE_LIBRARIAN")
                        .requestMatchers("/api/users/**").hasAuthority("ROLE_LIBRARIAN")
                        .requestMatchers("/api/borrows/overdue").hasAuthority("ROLE_LIBRARIAN")
                        .requestMatchers("/api/borrows/history").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_PATRON")
                        .requestMatchers("/api/borrows").hasAuthority("ROLE_PATRON")
                        .requestMatchers("/api/borrows/return").hasAuthority("ROLE_PATRON")
                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
