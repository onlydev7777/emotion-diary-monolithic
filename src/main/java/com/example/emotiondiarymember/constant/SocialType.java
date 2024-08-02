package com.example.emotiondiarymember.constant;

import java.util.Arrays;

public enum SocialType {
  GOOGLE, NAVER, KAKAO, GITHUB, KEYCLOAK, NONE;

  public static SocialType findByRegistrationId(String registrationId) {
    return Arrays.stream(SocialType.values())
        .filter(social -> social.name().toLowerCase().equals(registrationId))
        .findFirst()
        .orElse(NONE);
  }
}
