package com.example.emotiondiarymember.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@EqualsAndHashCode
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesDto {

  private Long id;
  private String resourceName;
  private HttpMethod httpMethod;
  private Set<Long> roleIds;

  public void setRoleIds(Set<Long> roleIds) {
    this.roleIds = roleIds;
  }
}
