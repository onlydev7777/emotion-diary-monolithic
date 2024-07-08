package com.example.emotiondiarymember.security.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public JwtAuthenticationFilter(String defaultFilterProcessesUrl) {
    super(defaultFilterProcessesUrl);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
    return null;
  }
}
