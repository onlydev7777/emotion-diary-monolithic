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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final static String HEADER_NAME = "Authorization";
  private final static String TOKEN_PREFIX = "Bearer ";
  private final String[] skipUrlList;
  private final JwtProvider jwtProvider;


  public JwtAuthorizationFilter(String[] SKIP_LIST, JwtProvider jwtProvider) {
    this.skipUrlList = SKIP_LIST;
    this.jwtProvider = jwtProvider;
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

    String token = resolveToken(request);
    String refreshToken = request.getHeader("Refresh-Token");
    Payload payload = jwtProvider.verifyToken(token);
    Jwt jwt = new Jwt(token, refreshToken);

    Authentication authenticated = LoginAuthentication.authenticated(payload, jwt, List.of());
    SecurityContextHolder.getContext().setAuthentication(authenticated);

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(HEADER_NAME);
    if (bearerToken == null) {
      throw new CustomAuthenticationException("Token is Not Empty!");
    }

    String decodedToken = URLDecoder.decode(bearerToken, StandardCharsets.UTF_8);
    if (decodedToken.startsWith(TOKEN_PREFIX)) {
      return decodedToken.substring(7);
    }

    throw new CustomAuthenticationException("Invalid Prefix Token!");
  }
}
