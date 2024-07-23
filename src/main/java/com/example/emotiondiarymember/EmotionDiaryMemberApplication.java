package com.example.emotiondiarymember;

import com.example.emotiondiarymember.security.jwt.JwtProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableConfigurationProperties({JwtProvider.class})
@SpringBootApplication
public class EmotionDiaryMemberApplication {

  public static void main(String[] args) {
    SpringApplication.run(EmotionDiaryMemberApplication.class, args);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
