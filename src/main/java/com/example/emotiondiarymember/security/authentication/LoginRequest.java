package com.example.emotiondiarymember.security.authentication;

import com.example.emotiondiarymember.constant.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

  private String id;
  private String password;
  private SocialType socialType;
}
