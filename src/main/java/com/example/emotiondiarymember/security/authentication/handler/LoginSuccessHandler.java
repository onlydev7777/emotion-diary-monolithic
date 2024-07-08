package com.example.emotiondiarymember.security.authentication.handler;

import com.example.emotiondiarymember.security.jwt.Jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    Jwt jwt = (Jwt) authentication.getPrincipal();

    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().println(new ObjectMapper().writeValueAsString(jwt));
  }
}
