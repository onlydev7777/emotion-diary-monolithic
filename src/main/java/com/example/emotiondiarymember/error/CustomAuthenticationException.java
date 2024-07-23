package com.example.emotiondiarymember.error;

import io.jsonwebtoken.JwtException;

public class CustomAuthenticationException extends JwtException {

  public CustomAuthenticationException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public CustomAuthenticationException(String msg) {
    super(msg);
  }
}
