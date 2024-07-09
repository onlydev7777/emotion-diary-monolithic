package com.example.emotiondiarymember.dto;

import com.example.emotiondiarymember.constant.SocialType;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

  private Long id;
  private String userId;
  private String password;
  private String name;
  private String email;
  private SocialType socialType;
  private Set<Long> roleIds;

  public void setRoleIds(Set<Long> roleIds) {
    this.roleIds = roleIds;
  }
}
