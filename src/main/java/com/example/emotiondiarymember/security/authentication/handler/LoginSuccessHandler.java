package com.example.emotiondiarymember.security.authentication.handler;

import com.example.emotiondiarymember.redis.RedisService;
import com.example.emotiondiarymember.security.authentication.LoginAuthentication;
import com.example.emotiondiarymember.security.jwt.Jwt;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import com.example.emotiondiarymember.security.jwt.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final RedisService redisService;
  private final JwtProvider jwtProvider;

  public LoginSuccessHandler(RedisService redisService, JwtProvider jwtProvider) {
    this.redisService = redisService;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    LoginAuthentication loginAuthentication = (LoginAuthentication) authentication;
    Jwt jwt = loginAuthentication.getJwt();
    Payload payload = (Payload) loginAuthentication.getPrincipal();

//    redisService.accessTokenSave(payload.getRedisKey(), jwt.getAccessToken());
    redisService.refreshTokenSave(payload.getRedisKey(), jwt.getRefreshToken());

    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

    response.setHeader(jwtProvider.getHeader(), jwtProvider.getTokenPrefix() + jwt.getAccessToken());
    response.setHeader(jwtProvider.getRefreshTokenHeader(), jwtProvider.getTokenPrefix() + jwt.getRefreshToken());
    PrintWriter writer = response.getWriter();
    writer.println(new ObjectMapper().writeValueAsString(jwt));
    writer.flush();
    writer.close();
  }
}
