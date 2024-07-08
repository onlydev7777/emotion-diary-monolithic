package com.example.emotiondiarymember.config;

import com.example.emotiondiarymember.security.authentication.handler.LoginSuccessHandler;
import com.example.emotiondiarymember.security.authentication.provider.LoginAuthenticationProvider;
import com.example.emotiondiarymember.security.filter.JwtAuthenticationFilter;
import com.example.emotiondiarymember.security.filter.LoginRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  //  private final CustomOAuth2UserService customOAuth2UserService;
//  private final CustomOidcUserService customOidcUserService;
  private final LoginAuthenticationProvider loginAuthenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(loginAuthenticationProvider);
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(request -> request
            .requestMatchers("/", "/login").permitAll()
            .anyRequest().authenticated())
        .logout(logout -> logout.logoutSuccessUrl("/"))
        .authenticationManager(authenticationManager)
        .addFilterBefore(loginRequestFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);

//    http
//        .oauth2Login(oauth2 -> oauth2
//            .defaultSuccessUrl("/")
//            .userInfoEndpoint(userInfo -> userInfo
//                .userService(customOAuth2UserService)
//                .oidcUserService(customOidcUserService)
//            )
//        );

    return http.build();
  }

  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter("/login");
    return jwtAuthenticationFilter;
  }

  public LoginRequestFilter loginRequestFilter(AuthenticationManager authenticationManager) {
    LoginRequestFilter loginRequestFilter = new LoginRequestFilter("/login");
    loginRequestFilter.setAuthenticationManager(authenticationManager);
    loginRequestFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
    return loginRequestFilter;
  }
}
