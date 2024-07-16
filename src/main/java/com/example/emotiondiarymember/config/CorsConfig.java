package com.example.emotiondiarymember.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:5173") // 허용할 오리진 설정
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 허용할 HTTP 메소드
        .allowedHeaders("*") // 허용할 헤더
        .allowCredentials(true) // 쿠키를 전송하려면 true
        .exposedHeaders(HttpHeaders.AUTHORIZATION, "Refresh-Token")
        .maxAge(3600); // 프리 플라이트 응답 캐싱 시간
  }
}
