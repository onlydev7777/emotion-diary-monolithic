package com.example.emotiondiarymember.security;

import com.example.emotiondiarymember.IntegrationTestSupport;
import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.security.authentication.LoginRequest;
import com.example.emotiondiarymember.security.jwt.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

public class SecurityTest extends IntegrationTestSupport {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void 로그인_테스트() {
    //given
    LoginRequest request = new LoginRequest("test", "qwer1234!", SocialType.NONE);

    //when
    ResponseEntity<Jwt> jwtResponseEntity = restTemplate.postForEntity("/login",
        request, Jwt.class);

    //then
    System.out.println("jwtResponseEntity.getStatusCode() = " + jwtResponseEntity.getStatusCode());
    System.out.println("jwtResponseEntity.getBody() = " + jwtResponseEntity.getBody());

  }
}
