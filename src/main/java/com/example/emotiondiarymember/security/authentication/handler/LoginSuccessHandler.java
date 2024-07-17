package com.example.emotiondiarymember.security.authentication.handler;

import com.example.emotiondiarymember.security.authentication.LoginAuthentication;
import com.example.emotiondiarymember.security.jwt.Jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    Jwt jwt = ((LoginAuthentication) authentication).getJwt();

    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

    response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getAccessToken());
    response.setHeader("Refresh-Token", "Bearer " + jwt.getRefreshToken());
    PrintWriter writer = response.getWriter();
    writer.println(new ObjectMapper().writeValueAsString(jwt));
    writer.flush();
    writer.close();
  }
}
