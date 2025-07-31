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
                        // 1) Public login/auth
                        .requestMatchers("/","/api/chat/auth/**").permitAll()

                        // 2) SockJS WS handshake
                        .requestMatchers(HttpMethod.GET, "/ws/**").permitAll()

                        // 3) Friend-list and search
                        .requestMatchers(HttpMethod.GET,  "/api/chat/users/friends").authenticated()
                        .requestMatchers(HttpMethod.GET,  "/api/chat/users/search").authenticated()
                        // ** ADDED: allow adding friends **
                        .requestMatchers(HttpMethod.POST, "/api/chat/users/*/friends").authenticated()

                        // 4) History endpoint
                        .requestMatchers(HttpMethod.GET, "/api/chat/history/**").authenticated()

                        // 5) (Optional) list all users
                        .requestMatchers(HttpMethod.GET, "/api/chat/users/**").authenticated()

                        .requestMatchers(HttpMethod.POST,   "/api/chat/requests/**").authenticated()
                        .requestMatchers(HttpMethod.GET,    "/api/chat/requests").authenticated()

                        // 6) Admin-only user mutations
                        .requestMatchers(HttpMethod.DELETE, "/api/chat/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/chat/users").hasAuthority("ADMIN")

                        // 7) Everything else requires authentication
                        .anyRequest().authenticated()
                )
                // Stateless session
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

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
