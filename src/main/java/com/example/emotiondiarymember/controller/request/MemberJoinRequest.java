package com.example.emotiondiarymember.controller.request;

import com.example.emotiondiarymember.constant.SocialType;
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
}
