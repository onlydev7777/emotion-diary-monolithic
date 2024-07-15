package com.example.emotiondiarymember.service;

import com.example.emotiondiarymember.dto.RoleDto;
import com.example.emotiondiarymember.entity.auth.Role;
import com.example.emotiondiarymember.mapper.RoleMapper;
import com.example.emotiondiarymember.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoleService {

  private final RoleRepository repository;
  private final RoleMapper mapper;

  public RoleDto save(RoleDto dto) {
    Role savedRole = repository.save(mapper.toEntity(dto));
    return mapper.toDto(savedRole);
  }

  public RoleDto findById(Long roleId) {
    Role role = repository.findById(roleId)
        .orElseThrow();
    
    return mapper.toDto(role);
  }
}
