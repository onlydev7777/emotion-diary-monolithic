package com.example.emotiondiarymember.security.jwt;

import com.example.emotiondiarymember.entity.embeddable.Email;
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
//  private Role role;
}
