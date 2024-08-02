package com.example.emotiondiarymember.security.dto.oauth;

import com.example.emotiondiarymember.constant.SocialType;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class NaverMember extends SocialMember {

  private final Map<String, Object> response;

  public NaverMember(SocialType socialType, OAuth2User oAuth2User) {
    super(socialType, oAuth2User);
    response = oAuth2User.getAttribute("response");
  }

  @Override
  public String getOAuthKey() {
    return (String) response.get("id");
  }

  @Override
  public String getUsername() {
    return (String) response.get("name");
  }

  @Override
  public String getEmail() {
    return (String) response.get("email");
  }
}
