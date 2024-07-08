package com.example.emotiondiarymember.security.filter;

import com.example.emotiondiarymember.security.authentication.LoginAuthentication;
import com.example.emotiondiarymember.security.authentication.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class LoginRequestFilter extends AbstractAuthenticationProcessingFilter {

  public LoginRequestFilter(String defaultFilterProcessesUrl) {
    super(defaultFilterProcessesUrl);
  }

  /**
   * '/login' 으로 들어오는 요청일 경우
   *   1. LoginRequest 로 파싱
   *   2. LoginAuthentication(미인증 상태) 으로 변환
   *   3. Authentication 에게 인증 위임
   *      : AuthenticationManager(ProviderManager) >> Provider(LoginAuthenticationProvider) 에서 인증 시도
   * @return
   * @throws AuthenticationException
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    LoginRequest loginRequest = new ObjectMapper().readValue(request.getReader(), LoginRequest.class);
    LoginAuthentication unauthenticated = LoginAuthentication.unauthenticated(loginRequest);

    return this.getAuthenticationManager().authenticate(unauthenticated);
  }
}
