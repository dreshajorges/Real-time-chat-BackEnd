package com.example.realtimechat.configs.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // public auth endpoints
                        .requestMatchers("/api/chat/auth/**").permitAll()
                        // allow SockJS handshake (GET /ws/**)
                        .requestMatchers(HttpMethod.GET, "/ws/**").permitAll()

                        // allow any authenticated user to **read** users (your “friends” list)
                        .requestMatchers(HttpMethod.GET, "/api/chat/users/**").authenticated()

                        // but only ADMIN can delete or update users
                        .requestMatchers(HttpMethod.DELETE, "/api/chat/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/chat/users/**").hasAuthority("ADMIN")

                        // everything else needs auth
                        .anyRequest().authenticated()
                )
                // make it stateless
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // your JWT filter
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(List.of("http://localhost:5173"));
        cors.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cors.setAllowCredentials(true);
        cors.setAllowedHeaders(List.of("*"));
        cors.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cors);
        return src;
    }
}
