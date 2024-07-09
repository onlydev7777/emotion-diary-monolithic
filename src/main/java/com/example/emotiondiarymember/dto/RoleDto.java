package com.example.emotiondiarymember.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

  private Long id;
  private String roleName;
  private String roleDescription;
}
