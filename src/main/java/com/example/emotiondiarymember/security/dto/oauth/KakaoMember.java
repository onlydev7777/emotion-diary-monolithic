package com.example.emotiondiarymember.security.dto.oauth;

import com.example.emotiondiarymember.constant.SocialType;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KakaoMember extends SocialMember {

  public KakaoMember(SocialType socialType, OAuth2User oAuth2User) {
    super(socialType, oAuth2User);
  }

  @Override
  public String getOAuthKey() {
    return oAuth2User.getAttribute("sub");
  }

  @Override
  public String getUsername() {
    return oAuth2User.getAttribute("nickname");
  }
}
