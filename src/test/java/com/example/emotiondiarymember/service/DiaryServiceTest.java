package com.example.emotiondiarymember.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.emotiondiarymember.IntegrationTestSupport;
import com.example.emotiondiarymember.TestComponent;
import com.example.emotiondiarymember.dto.DiaryDto;
import com.example.emotiondiarymember.dto.MemberDto;
import com.example.emotiondiarymember.entity.Diary;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.mapper.DiaryMapper;
import com.example.emotiondiarymember.mapper.MemberMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

class DiaryServiceTest extends IntegrationTestSupport {

  @Autowired
  private DiaryService service;
  @Autowired
  private MemberService memberService;
  @Autowired
  private TestComponent testComponent;
  @Autowired
  private DiaryMapper mapper;
  @Autowired
  private MemberMapper memberMapper;

  private MemberDto writerDto;
  private LocalDate now;
  private DiaryDto savedDto;

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    return Arrays.asList(
        DynamicTest.dynamicTest("글쓴이를 생성한다.", () -> saveWriter()),
        DynamicTest.dynamicTest("생성된 글쓴이가 일기를 작성한다.", () -> save()),
        DynamicTest.dynamicTest("작성된 일기가 정상적으로 조회된다.", () -> findById())
    );
  }

  void saveWriter() {
    testComponent.defaultSetUpMember();
    Member requestMember = testComponent.getMember();
    writerDto = memberService.save(memberMapper.toDto(requestMember, requestMember.getMemberRoles().stream()
        .map(mr -> mr.getRole().getId())
        .collect(Collectors.toSet())));

    Member writer = memberMapper.toEntity(writerDto);

    now = LocalDate.now();
    testComponent.defaultSetUpDiary(writer, now);
  }


  void save() {
    Diary requestDiary = testComponent.getDiary();
    DiaryDto requestDto = mapper.toDto(requestDiary);
    requestDto.setMemberId(writerDto.getId());
    savedDto = service.save(requestDto);

    assertThat(requestDiary.getSubject()).isEqualTo(savedDto.getSubject());
    assertThat(requestDiary.getContent()).isEqualTo(savedDto.getContent());
    assertThat(requestDiary.getEmotionStatus()).isEqualTo(savedDto.getEmotionStatus());
    assertThat(requestDiary.getDiaryDate()).isEqualTo(savedDto.getDiaryDate());
    assertThat(writerDto.getId()).isEqualTo(savedDto.getMemberId());
  }

  void findById() {
    Long savedDiaryId = savedDto.getId();
    DiaryDto findDto = service.findById(savedDiaryId);

    assertThat(savedDto).isEqualTo(findDto);
  }
}