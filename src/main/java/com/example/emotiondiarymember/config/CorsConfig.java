package com.example.emotiondiarymember.config;

import com.example.emotiondiarymember.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  private final JwtProvider jwtProvider;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:5173") // 허용할 오리진 설정
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 허용할 HTTP 메소드
        .allowedHeaders("*") // 허용할 헤더
        .allowCredentials(true) // 쿠키를 전송하려면 true
        .exposedHeaders(jwtProvider.getHeader(), jwtProvider.getRefreshTokenHeader())
        .maxAge(3600); // 프리 플라이트 응답 캐싱 시간
  }
}
