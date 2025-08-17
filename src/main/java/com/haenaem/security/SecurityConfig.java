package com.haenaem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(request -> {
          var corsConfig = new org.springframework.web.cors.CorsConfiguration();
          corsConfig.setAllowedOriginPatterns(java.util.List.of("*"));
          corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
          corsConfig.setAllowedHeaders(java.util.List.of("*"));
          corsConfig.setAllowCredentials(false);
          return corsConfig;
        }))
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().permitAll()  // 임시 모든 요청 허용
        )
        .formLogin(formLogin -> formLogin.disable())
        .httpBasic(httpBasic -> httpBasic.disable());

    return http.build();
  }
}