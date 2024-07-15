package com.example.emotiondiarymember.config;

import com.example.emotiondiarymember.security.authentication.handler.CustomAccessDeniedHandler;
import com.example.emotiondiarymember.security.authentication.handler.CustomAuthenticationFailureEntryPoint;
import com.example.emotiondiarymember.security.authentication.handler.LoginSuccessHandler;
import com.example.emotiondiarymember.security.authentication.provider.LoginAuthenticationProvider;
import com.example.emotiondiarymember.security.filter.JwtAuthorizationFilter;
import com.example.emotiondiarymember.security.filter.LoginRequestFilter;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  //  private final CustomOAuth2UserService customOAuth2UserService;
//  private final CustomOidcUserService customOidcUserService;
  private final LoginAuthenticationProvider loginAuthenticationProvider;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final CustomAuthenticationFailureEntryPoint customAuthenticationFailureEntryPoint;
  private final JwtProvider jwtProvider;

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;
  private final String[] SKIP_LIST = {"/", "/login", "/login-test"};

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
            .requestMatchers(SKIP_LIST).permitAll()
            .requestMatchers("/admin").hasRole("ADMIN")
            .anyRequest().authenticated())
        .logout(logout -> logout.logoutSuccessUrl("/"))
        .authenticationManager(authenticationManager)
        .exceptionHandling(ex -> ex
            .accessDeniedHandler(customAccessDeniedHandler)
            .authenticationEntryPoint(customAuthenticationFailureEntryPoint)
        )
        .addFilterBefore(loginRequestFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

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

  public JwtAuthorizationFilter jwtAuthenticationFilter() {
    JwtAuthorizationFilter jwtAuthenticationFilter = new JwtAuthorizationFilter(SKIP_LIST, jwtProvider, resolver);
    return jwtAuthenticationFilter;
  }

  public LoginRequestFilter loginRequestFilter(AuthenticationManager authenticationManager) {
    LoginRequestFilter loginRequestFilter = new LoginRequestFilter("/login");
    loginRequestFilter.setAuthenticationManager(authenticationManager);
    loginRequestFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
    return loginRequestFilter;
  }
}
