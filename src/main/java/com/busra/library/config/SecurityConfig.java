
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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/v3/api-docs.yaml",
                                "/swagger-resources/**",
                                "/swagger-resources",
                                "/webjars/**",
                                "/configuration/**"
                        ).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/books").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/books/search/**").hasAnyAuthority("LIBRARIAN", "PATRON")
                        .requestMatchers(HttpMethod.GET, "/api/books/{id}").hasAnyAuthority("LIBRARIAN", "PATRON")
                        .requestMatchers(HttpMethod.GET, "/api/books").hasAnyAuthority("LIBRARIAN", "PATRON")
                        .requestMatchers(HttpMethod.PATCH, "/api/reactive/books/{id}/availability").hasAuthority("LIBRARIAN")
                        .requestMatchers("/api/users/**").hasAuthority("LIBRARIAN")
                        .requestMatchers("/api/borrows/overdue").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/borrows/history").hasAnyAuthority("LIBRARIAN", "PATRON")
                        .requestMatchers(HttpMethod.POST, "/api/borrows", "/api/borrows/return").hasAuthority("PATRON")
                        .requestMatchers(HttpMethod.GET, "/api/borrows/users/me/borrow-history").hasAuthority("PATRON")
                        .requestMatchers(HttpMethod.GET, "/api/borrows/users/{userId}/borrow-history").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/borrows/overdue").hasAuthority("LIBRARIAN")
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
