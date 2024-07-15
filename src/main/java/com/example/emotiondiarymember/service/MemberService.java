package com.example.emotiondiarymember.service;

import com.example.emotiondiarymember.dto.MemberDto;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.entity.auth.MemberRole;
import com.example.emotiondiarymember.mapper.MemberMapper;
import com.example.emotiondiarymember.repository.MemberRepository;
import com.example.emotiondiarymember.repository.MemberRoleRepository;
import com.example.emotiondiarymember.repository.RoleRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

  public MemberDto save(MemberDto dto) {
    Member savedMember = repository.save(mapper.toEntity(dto));
    roleRepository.findAllById(dto.getRoleIds()).forEach(
        role -> memberRoleRepository.save(MemberRole.of(savedMember, role))
    );

    return mapper.toDto(savedMember, dto.getRoleIds());
  }

  public MemberDto findById(Long memberId) {
    Member member = repository.findById(memberId)
        .orElseThrow();

    return mapper.toDto(member, member.getMemberRoles().stream()
        .map(mr -> mr.getRole().getId())
        .collect(Collectors.toSet()));
  }
}
