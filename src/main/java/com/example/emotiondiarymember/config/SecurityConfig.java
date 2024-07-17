package com.example.emotiondiarymember.config;

import com.example.emotiondiarymember.security.authentication.handler.CustomAccessDeniedHandler;
import com.example.emotiondiarymember.security.authentication.handler.CustomAuthenticationFailureEntryPoint;
import com.example.emotiondiarymember.security.authentication.handler.LoginFailureHandler;
import com.example.emotiondiarymember.security.authentication.handler.LoginSuccessHandler;
import com.example.emotiondiarymember.security.authentication.provider.LoginAuthenticationProvider;
import com.example.emotiondiarymember.security.filter.JwtAuthorizationFilter;
import com.example.emotiondiarymember.security.filter.LoginRequestFilter;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Jwt 인증에 대한 오류 처리 방법
 *
 * 1. Exception 을 receive 하는 ReceivingJwtExceptionFilter 에서 처리
 *    : AuthenticationException 인증 오류
 *    : AccessDeniedException 인가 오류
 *    : RuntimeException 런타임 오류
 *
 * 2. Security 에서 제공해주는 ExceptionTranslationFilter 의 handleSpringSecurityException() method 에서 처리
 *    : AuthenticationException 인증 오류
 *      > CustomAuthenticationFailureEntryPoint 에서 처리
 *    : AccessDeniedException 인가 오류
 *      > CustomAccessDeniedHandler 에서 처리
 *
 * 3. GlobalExceptionHandler 에서 한번에 처리
 *    : 의존성 주입 받아서 사용
 *      @Autowired 주입
 *      @Qualifier("handlerExceptionResolver")
 *      private HandlerExceptionResolver resolver;
 *
 *    : Filter 단에서 try-catch 로 받아서 resolveException 호출
 *      > resolver.resolveException(request, response, null, e);
 *
 *
 */
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
  private final String[] SKIP_LIST = {"/", "/login", "/login-test", "/error"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(loginAuthenticationProvider);
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
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
        .addFilterBefore(jwtAuthorizationFilter(), AuthorizationFilter.class);
//        .addFilterBefore(new ReceivingJwtExceptionFilter(), JwtAuthorizationFilter.class);

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

  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    JwtAuthorizationFilter jwtAuthenticationFilter = new JwtAuthorizationFilter(SKIP_LIST, jwtProvider);
    return jwtAuthenticationFilter;
  }

  public LoginRequestFilter loginRequestFilter(AuthenticationManager authenticationManager) {
    LoginRequestFilter loginRequestFilter = new LoginRequestFilter("/login");
    loginRequestFilter.setAuthenticationManager(authenticationManager);
    loginRequestFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
    loginRequestFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
    return loginRequestFilter;
  }
}
