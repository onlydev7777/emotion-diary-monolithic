package com.example.emotiondiarymember.mapper;

import com.example.emotiondiarymember.dto.MemberDto;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.entity.embeddable.Email;
import com.example.emotiondiarymember.entity.embeddable.Password;
import java.util.Set;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface MemberMapper {

  //  @Mapping(target = "password", expression = "java(toPassword(dto.getPassword(), passwordEncoder))")
//  @Mapping(target = "email", expression = "java(toEmail(dto.getEmail()))")
  Member toEntity(MemberDto dto);

  //    @Mapping(target = "password", ignore = true)
//  @Mapping(target = "email", expression = "java(member.getEmail().getEmail())")
  @Mapping(target = "roleIds", expression = "java(roleIds)")
  MemberDto toDto(Member member, @Context Set<Long> roleIds);

  default Password toPassword(String password, PasswordEncoder passwordEncoder) {
    return new Password(password, passwordEncoder);
  }

  default Email toEmail(String email) {
    return new Email(email);
  }
}
