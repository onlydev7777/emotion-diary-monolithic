package com.example.emotiondiarymember.config;

import com.example.emotiondiarymember.security.jwt.JwtProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
//public class CorsConfig implements WebMvcConfigurer {
public class CorsConfig {

  private final JwtProvider jwtProvider;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:8081");//localhost:8081 에서의 요청 허용
    configuration.addAllowedHeader("*");//Request Header 전체 허용
    configuration.addAllowedMethod("*");//HTTP 메서드 전체 허용
    configuration.setAllowCredentials(true);//쿠키 전송 허용
    configuration.setMaxAge(3600L);//Preflight Request 캐싱 시간 : 1시간
    configuration.setExposedHeaders(List.of(jwtProvider.getAccessTokenHeader(), jwtProvider.getRefreshTokenHeader()));//Response Header 허용

    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);  // 모든 url 패턴 허용
    return urlBasedCorsConfigurationSource;
  }

//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**")
//        .allowedOrigins("http://localhost:8081") // 허용할 오리진 설정
//        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 허용할 HTTP 메소드
//        .allowedHeaders("*") // 허용할 헤더
//        .allowCredentials(true) // 쿠키를 전송하려면 true
//        .exposedHeaders(jwtProvider.getAccessTokenHeader(), jwtProvider.getRefreshTokenHeader())
//        .maxAge(3600); // 프리 플라이트 응답 캐싱 시간
//  }
}
