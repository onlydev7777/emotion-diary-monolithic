package com.example.emotiondiarymember.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jwt {

  private String accessToken;
  private String refreshToken;

}
