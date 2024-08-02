package com.example.emotiondiarymember.controller.request;

import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.security.dto.oauth.SocialMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinRequest {

  private String userId;
  private String password;
  private String name;
  private String email;
  private SocialType socialType;

  public static MemberJoinRequest of(SocialMember socialMember) {
    System.out.println("socialMember.getOAuthKey() = " + socialMember.getOAuthKey());
    String password = "1social^" + socialMember.getSocialType().name();
    int length = password.length();
    if (length > 16) {
      length = 16;
    }
    return MemberJoinRequest.builder()
        .userId(socialMember.getEmail())
        .socialType(socialMember.getSocialType())
        .name(socialMember.getUsername().replaceAll("\\s", ""))
        .email(socialMember.getEmail())
        .password(password.substring(0, length))
        .build();
  }
}
