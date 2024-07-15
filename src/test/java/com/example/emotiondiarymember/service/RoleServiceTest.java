package com.example.emotiondiarymember.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.emotiondiarymember.IntegrationTestSupport;
import com.example.emotiondiarymember.TestComponent;
import com.example.emotiondiarymember.dto.RoleDto;
import com.example.emotiondiarymember.entity.auth.Role;
import com.example.emotiondiarymember.mapper.RoleMapper;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

class RoleServiceTest extends IntegrationTestSupport {

  @Autowired
  private TestComponent testComponent;
  @Autowired
  private RoleService service;
  @Autowired
  private RoleMapper mapper;

  private RoleDto savedRoleDto;

  @BeforeEach
  void setUp() {
    testComponent.defaultSetUpRole();
  }

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    return Arrays.asList(
        DynamicTest.dynamicTest("입력된 권한이 저장된다.", () -> save()),
        DynamicTest.dynamicTest("입력된 권한이 조회된다.", () -> findById())
    );
  }

  //  @Test
  void save() {
    Role requestRole = testComponent.getRole();
    savedRoleDto = service.save(mapper.toDto(requestRole));

    assertThat(requestRole.getRoleName()).isEqualTo(savedRoleDto.getRoleName());
    assertThat(requestRole.getRoleDescription()).isEqualTo(savedRoleDto.getRoleDescription());
  }

  //  @Test
  void findById() {
    Long savedRoleId = savedRoleDto.getId();
    RoleDto findDto = service.findById(savedRoleId);

    assertThat(savedRoleDto).isEqualTo(findDto);
  }
}