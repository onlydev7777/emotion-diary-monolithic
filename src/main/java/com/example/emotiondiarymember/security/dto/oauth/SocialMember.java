package com.example.emotiondiarymember.security.dto.oauth;

import com.example.emotiondiarymember.constant.SocialType;
import lombok.Getter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public abstract class SocialMember {

  private final SocialType socialType;
  protected final OAuth2User oAuth2User;

  protected SocialMember(SocialType socialType, OAuth2User oAuth2User) {
    this.socialType = socialType;
    this.oAuth2User = oAuth2User;
  }

  public abstract String getOAuthKey();

  public abstract String getUsername();

  public String getEmail() {
    return oAuth2User.getAttribute("email");
  }

  public static SocialMember of(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
    SocialType socialType = SocialType.findByRegistrationId(clientRegistration.getRegistrationId());
    switch (socialType) {
      case KAKAO -> {
        return new KakaoMember(socialType, oAuth2User);
      }
      case NAVER -> {
        return new NaverMember(socialType, oAuth2User);
      }
      case GOOGLE -> {
        return new GoogleMember(socialType, oAuth2User);
      }
      case KEYCLOAK -> {
        return new KeycloakMember(socialType, oAuth2User);
      }
      case GITHUB -> {
        return null;
      }
    }
    return null;
  }
}
