package com.example.emotiondiarymember.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.emotiondiarymember.IntegrationTestSupport;
import com.example.emotiondiarymember.TestComponent;
import com.example.emotiondiarymember.dto.MemberDto;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.mapper.MemberMapper;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends IntegrationTestSupport {

  @Autowired
  private MemberService service;
  @Autowired
  private TestComponent testComponent;
  @Autowired
  private MemberMapper mapper;

  private MemberDto savedMemberDto;

  @BeforeEach
  void setUp() {
    testComponent.defaultSetUpMember();
  }

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    return Arrays.asList(
        DynamicTest.dynamicTest("회원가입이 정상적으로 수행된다.", () -> save()),
        DynamicTest.dynamicTest("회원가입 된 계정으로 정상조회가 가능하다.", () -> findById())
    );
  }

  //  @DisplayName("회원가입이 정상적으로 수행된다.")
//  @Test
  void save() {
    Member requestMember = testComponent.getMember();
    savedMemberDto = service.save(mapper.toDto(requestMember, requestMember.getMemberRoles().stream()
        .map(mr -> mr.getRole().getId())
        .collect(Collectors.toSet())));

    assertThat(requestMember.getName()).isEqualTo(savedMemberDto.getName());
    assertThat(requestMember.getEmail()).isEqualTo(savedMemberDto.getEmail());
    assertThat(requestMember.getPassword()).isEqualTo(savedMemberDto.getPassword());
    assertThat(requestMember.getSocialType()).isEqualTo(savedMemberDto.getSocialType());
  }

  //  @DisplayName("회원가입 된 계정으로 정상조회가 가능하다.")
//  @Test
  void findById() {
    Long savedMemberId = savedMemberDto.getId();
    MemberDto findDto = service.findById(savedMemberId);

    Assertions.assertThat(savedMemberDto).isEqualTo(findDto);
  }
}
