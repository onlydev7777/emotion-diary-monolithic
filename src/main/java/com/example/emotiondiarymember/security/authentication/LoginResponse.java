package com.example.emotiondiarymember.security.authentication;

import com.example.emotiondiarymember.security.jwt.Jwt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

  private Jwt jwt;
  private Long id;
}
