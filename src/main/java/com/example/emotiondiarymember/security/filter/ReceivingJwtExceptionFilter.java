package com.example.emotiondiarymember.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ReceivingJwtExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException eje) { // JWT 만료 오류
      log.error(eje.getMessage(), eje);
      sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Access-Token is expired");
    } catch (JwtException je) {  // JWT 인증 오류
      log.error(je.getMessage(), je);
      sendError(response, HttpServletResponse.SC_UNAUTHORIZED, je.getMessage());
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
