package com.example.emotiondiarymember.config;

import com.example.emotiondiarymember.redis.RedisService;
import com.example.emotiondiarymember.security.authentication.handler.CustomAccessDeniedHandler;
import com.example.emotiondiarymember.security.authentication.handler.CustomAuthenticationFailureEntryPoint;
import com.example.emotiondiarymember.security.authentication.handler.LoginFailureHandler;
import com.example.emotiondiarymember.security.authentication.handler.LoginSuccessHandler;
import com.example.emotiondiarymember.security.authentication.provider.LoginAuthenticationProvider;
import com.example.emotiondiarymember.security.filter.JwtAuthorizationFilter;
import com.example.emotiondiarymember.security.filter.LoginRequestFilter;
import com.example.emotiondiarymember.security.filter.ReceivingJwtExceptionFilter;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import com.example.emotiondiarymember.security.service.CustomOAuth2UserService;
import com.example.emotiondiarymember.security.service.CustomOidcUserService;
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

/*
 *
 * - 로그인 요청에 대한 오류 처리 방법 : 1번 방식 사용
 *    1. AbstractAuthenticationProcessingFilter 에서 처리
 *       : AuthenticationException 인증 오류 > LoginFailureHandler 에서 처리
 *         => 로그인 요청에 대한 실패
 *            ex) 존재하지 않는 아이디, 패스워드 오류 등
 *
 * - Jwt 인증에 대한 오류 처리 방법 : 1, 2번 방식 사용
 *
 *    1. Exception 을 receive 하는 ReceivingJwtExceptionFilter 에서 처리
 *       : ExpiredJwtException 토큰 만기(expired) 오류
 *       : JwtException 토큰 오류
 *
 *    2. Security 에서 제공해주는 ExceptionTranslationFilter 의 handleSpringSecurityException() method 에서 처리
 *       : AuthenticationException 인증 오류 > CustomAuthenticationFailureEntryPoint 에서 처리
 *         => 미인증 사용자 요청 오류
 *       : AccessDeniedException 인가 오류 > CustomAccessDeniedHandler 에서 처리
 *         => 인증 사용자 권한 오류
 *
 *    3. GlobalExceptionHandler 에서 한번에 처리 : 의존성 주입 받아서 사용
 *
 *       @Autowired 주입
 *       @Qualifier("handlerExceptionResolver")
 *       private HandlerExceptionResolver resolver;
 *       : Filter 단에서 try-catch 로 받아서 resolveException 호출
 *         => resolver.resolveException(request, response, null, e);
 *
 *       => 해당 방식은 사용하지 않음.
 *          하나의 Handler 에서 관리 될 수 있는 장점은 있으나,
 *          Security 의 Exception 처리까지 해당 Handler 에서 관리하는게 단점이 더 많아보임
 *          GlobalExceptionHandler 는 MVC 모델의 비즈니스 서비스에 대한 공통 오류 처리를 담당
 *
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomOidcUserService customOidcUserService;

  private final LoginAuthenticationProvider loginAuthenticationProvider;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final CustomAuthenticationFailureEntryPoint customAuthenticationFailureEntryPoint;
  private final JwtProvider jwtProvider;
  private final RedisService redisService;
  private final String[] SKIP_LIST = {"/", "/login", "/member/signup", "/auth/**", "/error",
      "/index", "/oauth2/**", "/actuator/**",
      "/js/**", "/images/**", "/css/**", "/scss/**", "/favicon.ico"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(loginAuthenticationProvider);
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
    LoginSuccessHandler successHandler = new LoginSuccessHandler(redisService, jwtProvider);

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
//        .logout(logout -> logout.logoutSuccessUrl("/signin"))
        .authenticationManager(authenticationManager)
        .exceptionHandling(ex -> ex
            .accessDeniedHandler(customAccessDeniedHandler)
            .authenticationEntryPoint(customAuthenticationFailureEntryPoint)
        )
        .addFilterBefore(loginRequestFilter(authenticationManager, successHandler), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtAuthorizationFilter(), AuthorizationFilter.class)
        .addFilterBefore(receivingJwtExceptionFilter(), JwtAuthorizationFilter.class);

    http
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
                .oidcUserService(customOidcUserService)
            )
            .successHandler(successHandler)
        );

    return http.build();
  }

  private ReceivingJwtExceptionFilter receivingJwtExceptionFilter() {
    return new ReceivingJwtExceptionFilter();
  }

  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    JwtAuthorizationFilter jwtAuthenticationFilter = new JwtAuthorizationFilter(SKIP_LIST, redisService, jwtProvider);
    return jwtAuthenticationFilter;
  }

  public LoginRequestFilter loginRequestFilter(AuthenticationManager authenticationManager, LoginSuccessHandler successHandler) {
    LoginRequestFilter loginRequestFilter = new LoginRequestFilter("/login");
    loginRequestFilter.setAuthenticationManager(authenticationManager);
    loginRequestFilter.setAuthenticationSuccessHandler(successHandler);
    loginRequestFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
    return loginRequestFilter;
  }

}
