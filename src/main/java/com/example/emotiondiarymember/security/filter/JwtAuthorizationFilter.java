package com.example.emotiondiarymember.security.filter;

import com.example.emotiondiarymember.error.CustomAuthenticationException;
import com.example.emotiondiarymember.security.authentication.LoginAuthentication;
import com.example.emotiondiarymember.security.jwt.Jwt;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import com.example.emotiondiarymember.security.jwt.Payload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final static String HEADER_NAME = "Authorization";
  private final static String TOKEN_PREFIX = "Bearer ";
  private final String[] skipUrlList;
  private final JwtProvider jwtProvider;

  private final HandlerExceptionResolver resolver;

  public JwtAuthorizationFilter(String[] SKIP_LIST, JwtProvider jwtProvider, HandlerExceptionResolver resolver) {
    this.skipUrlList = SKIP_LIST;
    this.jwtProvider = jwtProvider;
    this.resolver = resolver;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    boolean skip = Arrays.stream(skipUrlList)
        .anyMatch(url -> url.equals(request.getRequestURI()));
    if (skip) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String token = resolveToken(request);
      String refreshToken = request.getHeader("Refresh-Token");
      Payload payload = jwtProvider.verifyToken(token);
      Jwt jwt = new Jwt(token, refreshToken);

      Authentication authenticated = LoginAuthentication.authenticated(payload, jwt, List.of(payload.getRole()));
      SecurityContextHolder.getContext().setAuthentication(authenticated);

      filterChain.doFilter(request, response);
    } catch (AuthenticationException e) {
      resolver.resolveException(request, response, null, e);
    }
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(HEADER_NAME);
    if (bearerToken != null) {
      String decodedToken = URLDecoder.decode(bearerToken, StandardCharsets.UTF_8);
      if (decodedToken.startsWith(TOKEN_PREFIX)) {
        return decodedToken.substring(7);
      }
    }
    throw new CustomAuthenticationException("Token Parsing Error!");
  }
}
