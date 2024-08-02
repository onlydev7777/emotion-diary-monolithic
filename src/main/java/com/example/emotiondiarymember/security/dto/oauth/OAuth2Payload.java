package com.example.emotiondiarymember.security.dto.oauth;

import com.example.emotiondiarymember.security.jwt.Payload;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class OAuth2Payload implements OAuth2User, OidcUser {

  private final SocialMember socialMember;
  private final Payload payload;

  public OAuth2Payload(SocialMember socialMember, Payload payload) {
    this.socialMember = socialMember;
    this.payload = payload;
  }

  @Override
  public <A> A getAttribute(String name) {
    return socialMember.getOAuth2User().getAttribute(name);
  }

  @Override
  public Map<String, Object> getAttributes() {
    return socialMember.getOAuth2User().getAttributes();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return socialMember.getOAuth2User().getAuthorities() == null ? List.of() : socialMember.getOAuth2User().getAuthorities();
  }

  @Override
  public String getName() {
    return socialMember.getOAuth2User().getName();
  }

  @Override
  public Map<String, Object> getClaims() {
    return ((OidcUser) socialMember.getOAuth2User()).getClaims();
  }

  @Override
  public OidcUserInfo getUserInfo() {
    return ((OidcUser) socialMember.getOAuth2User()).getUserInfo();
  }

  @Override
  public OidcIdToken getIdToken() {
    return ((OidcUser) socialMember.getOAuth2User()).getIdToken();
  }
}
