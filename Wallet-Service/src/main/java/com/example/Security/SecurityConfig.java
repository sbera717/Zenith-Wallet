package com.example.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


        public static final String ADMIN = "admin";
        public static final String USER = "user";
        private final JwtConverter jwtConverter;
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests((auth) ->auth
                            .anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults())
                    .csrf(AbstractHttpConfigurer::disable)
                    .formLogin(Customizer.withDefaults());
            http.sessionManagement(sess -> sess.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS));
            http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

            return http.build();
        }

    }

