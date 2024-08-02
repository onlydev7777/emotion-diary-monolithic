package com.example.emotiondiarymember.security.jwt;

import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.entity.embeddable.Email;
import com.example.emotiondiarymember.security.dto.MemberDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payload {

  private Long id;
  private String userId;
  private Email email;
  private SocialType socialType;
//  private Role role;

  public String getRedisKey() {
    return this.id + "/" + this.userId + "/" + this.email.getEmail() + "/" + this.socialType;
  }

  public static Payload of(MemberDetails memberDetails) {
    return new Payload(memberDetails.getId(), memberDetails.getUserId(), memberDetails.getEmail(), memberDetails.getSocialType());
  }
}
