package com.example.emotiondiarymember.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Jwt 인증에 대한 오류 처리 방법
 *
 * 1. Exception 을 receive 하는 ReceivingJwtExceptionFilter 에서 처리
 *    : AuthenticationException 인증 오류
 *    : AccessDeniedException 인가 오류
 *    : RuntimeException 런타임 오류
 *
 * 2. Security 에서 제공해주는 ExceptionTranslationFilter 의 handleSpringSecurityException() method 에서 처리
 *    : AuthenticationException 인증 오류
 *      > CustomAuthenticationFailureEntryPoint 에서 처리
 *    : AccessDeniedException 인가 오류
 *      > CustomAccessDeniedHandler 에서 처리
 */
public class ReceivingJwtExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (AuthenticationException ae) {  // 인증 오류
      sendError(response, HttpServletResponse.SC_UNAUTHORIZED, ae.getMessage());
    } catch (AccessDeniedException ade) {   // 인가 오류
      sendError(response, HttpServletResponse.SC_FORBIDDEN, ade.getMessage());
    } catch (RuntimeException re) {         // 런타임 오류
      sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, re.getMessage());
    }
  }

  private void sendError(HttpServletResponse response, int errorCode, String errorMessage) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.setStatus(errorCode);

    PrintWriter writer = response.getWriter();
    writer.write(errorMessage);
    writer.flush();
    writer.close();
  }
}
