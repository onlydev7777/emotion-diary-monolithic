package com.example.emotiondiarymember.mapper;

import com.example.emotiondiarymember.dto.RoleDto;
import com.example.emotiondiarymember.entity.auth.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  Role toEntity(RoleDto dto);

  RoleDto toDto(Role role);
}
