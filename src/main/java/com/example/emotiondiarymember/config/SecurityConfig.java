package com.example.emotiondiarymember.config;

import com.example.emotiondiarymember.security.CustomOAuth2UserService;
import com.example.emotiondiarymember.security.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomOidcUserService customOidcUserService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(request -> request
            .requestMatchers("/").permitAll()
            .anyRequest().authenticated())
        .logout(logout -> logout.logoutSuccessUrl("/"));

    http
        .oauth2Login(oauth2 -> oauth2
            .defaultSuccessUrl("/")
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
                .oidcUserService(customOidcUserService)
            )
        );

    return http.build();
  }
}
