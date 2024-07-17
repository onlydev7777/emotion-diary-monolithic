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
