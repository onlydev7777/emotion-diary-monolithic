package com.example.emotiondiarymember.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.emotiondiarymember.IntegrationTestSupport;
import com.example.emotiondiarymember.TestComponent;
import com.example.emotiondiarymember.dto.ResourcesDto;
import com.example.emotiondiarymember.entity.auth.Resources;
import com.example.emotiondiarymember.mapper.ResourcesMapper;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

class ResourcesServiceTest extends IntegrationTestSupport {

  @Autowired
  private TestComponent testComponent;
  @Autowired
  private ResourcesService service;
  @Autowired
  private ResourcesMapper mapper;

  private ResourcesDto savedResourcesDto;

  @BeforeEach
  void setUp() {
    testComponent.defaultSetUpResources();
  }

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    return Arrays.asList(
        DynamicTest.dynamicTest("URL 자원이 정상 입력된다.", () -> save()),
        DynamicTest.dynamicTest("입력된 URL 자원에 대한 정상조회가 가능하다.", () -> findById())
    );
  }

  //  @Test
  void save() {
    Resources requestResources = testComponent.getResources();
    savedResourcesDto = service.save(mapper.toDto(requestResources, requestResources.getRoleResources().stream()
        .map(rr -> rr.getRole().getId())
        .collect(Collectors.toSet())));

    assertThat(requestResources.getResourceName()).isEqualTo(savedResourcesDto.getResourceName());
    assertThat(requestResources.getHttpMethod()).isEqualTo(savedResourcesDto.getHttpMethod());
  }

  //  @Test
  void findById() {
    Long savedResourcesId = savedResourcesDto.getId();
    ResourcesDto findDto = service.findById(savedResourcesId);

    assertThat(savedResourcesDto).isEqualTo(findDto);
  }
}