package com.example.emotiondiarymember.security.authentication;

import com.example.emotiondiarymember.security.jwt.Jwt;
import com.example.emotiondiarymember.security.jwt.Payload;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;

public class LoginAuthentication extends AbstractAuthenticationToken {

  private Object principal;
  private Jwt jwt;
  private Object credentials;

  public LoginAuthentication() {
    super(null);
  }

  public LoginAuthentication(Object principal, Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.jwt = jwt;
    this.credentials = null;
    setAuthenticated(true);
  }

  public LoginAuthentication(Object principal, Object credentials) {
    super(null);
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(false);
  }

  public static LoginAuthentication unauthenticated(LoginRequest request) {
    String principal = request.getId() + "^" + request.getSocialType().name();
    String credentials = request.getPassword();

    validateCheck(principal, credentials);
    return new LoginAuthentication(principal, credentials);
  }

  public static LoginAuthentication authenticated(Payload payload, Jwt jwt, List<? extends GrantedAuthority> authorities) {
    return new LoginAuthentication(payload, jwt, authorities);
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  @Override
  public Object getCredentials() {
    return credentials;
  }

  public Jwt getJwt() {
    return jwt;
  }

  private static void validateCheck(String principal, String credentials) {
    if (principal == null) {
      throw new BadCredentialsException("ID는 필수입니다.");
    }
    if (credentials == null) {
      throw new BadCredentialsException("Password는 필수입니다.");
    }
  }
}
