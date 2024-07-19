package com.example.emotiondiarymember.security.util;

import com.example.emotiondiarymember.security.jwt.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class TokenUtil {

  public static Payload getPayload() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalArgumentException("Authentication Token is null");
    }
    return (Payload) authentication.getPrincipal();
  }

  public static Long getMemberId() {
    return getPayload().getId();
  }

  public static String getUserId() {
    return getPayload().getUserId();
  }
}
