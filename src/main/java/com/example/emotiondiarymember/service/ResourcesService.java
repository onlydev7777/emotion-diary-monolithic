package com.example.emotiondiarymember.service;

import com.example.emotiondiarymember.dto.ResourcesDto;
import com.example.emotiondiarymember.entity.auth.Resources;
import com.example.emotiondiarymember.entity.auth.RoleResources;
import com.example.emotiondiarymember.mapper.ResourcesMapper;
import com.example.emotiondiarymember.repository.ResourcesRepository;
import com.example.emotiondiarymember.repository.RoleRepository;
import com.example.emotiondiarymember.repository.RoleResourcesRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResourcesService {

  private final ResourcesRepository repository;
  private final RoleRepository roleRepository;
  private final RoleResourcesRepository roleResourcesRepository;
  private final ResourcesMapper mapper;

  public ResourcesDto save(ResourcesDto dto) {
    Resources savedResources = repository.save(mapper.toEntity(dto));
    roleRepository.findAllById(dto.getRoleIds()).forEach(
        role -> roleResourcesRepository.save(RoleResources.of(role, savedResources))
    );
    return mapper.toDto(savedResources, dto.getRoleIds());
  }

  public ResourcesDto findById(Long resourceId) {
    Resources resources = repository.findById(resourceId)
        .orElseThrow();

    return mapper.toDto(resources, resources.getRoleResources().stream()
        .map(rr -> rr.getRole().getId())
        .collect(Collectors.toSet()));
  }
}
