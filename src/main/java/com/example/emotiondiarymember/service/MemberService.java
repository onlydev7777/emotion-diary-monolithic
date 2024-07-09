package com.example.emotiondiarymember.service;

import com.example.emotiondiarymember.dto.MemberDto;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.entity.auth.MemberRole;
import com.example.emotiondiarymember.mapper.MemberMapper;
import com.example.emotiondiarymember.repository.MemberRepository;
import com.example.emotiondiarymember.repository.MemberRoleRepository;
import com.example.emotiondiarymember.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

  private final MemberRepository repository;
  private final RoleRepository roleRepository;
  private final MemberRoleRepository memberRoleRepository;
  private final MemberMapper mapper;
  private final PasswordEncoder passwordEncoder;

  public MemberDto save(MemberDto dto) {
    Member savedMember = repository.save(mapper.toEntity(dto, passwordEncoder));
    roleRepository.findAllById(dto.getRoleIds()).forEach(
        role -> memberRoleRepository.save(MemberRole.of(savedMember, role))
    );

    return mapper.toDto(savedMember, dto.getRoleIds());
  }
}
