package com.example.emotiondiarymember.mapper;

import com.example.emotiondiarymember.dto.ResourcesDto;
import com.example.emotiondiarymember.entity.auth.Resources;
import java.util.Set;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResourcesMapper {

  Resources toEntity(ResourcesDto dto);

  @Mapping(target = "roleIds", expression = "java(roleIds)")
  ResourcesDto toDto(Resources resources, @Context Set<Long> roleIds);
}
