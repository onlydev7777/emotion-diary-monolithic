package com.example.emotiondiarymember.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.emotiondiarymember.IntegrationTestSupport;
import com.example.emotiondiarymember.TestComponent;
import com.example.emotiondiarymember.constant.EmotionStatus;
import com.example.emotiondiarymember.dto.DiaryDto;
import com.example.emotiondiarymember.dto.MemberDto;
import com.example.emotiondiarymember.entity.Diary;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.mapper.DiaryMapper;
import com.example.emotiondiarymember.mapper.MemberMapper;
import com.example.emotiondiarymember.util.DateUtil;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
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
        DynamicTest.dynamicTest("작성된 일기가 정상적으로 조회된다.", () -> findById()),
        DynamicTest.dynamicTest("작성된 일기가 년/월을 기준으로 정상 조회 된다.", () -> findByYearMonth()),
        DynamicTest.dynamicTest("작성된 일기를 수정 할 수 있다.", () -> update()),
        DynamicTest.dynamicTest("작성된 일기를 삭제 할 수 있다.", () -> delete())
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
    assertThat(requestDiary.getDiaryYearMonth()).isEqualTo(savedDto.getDiaryYearMonth());
    assertThat(writerDto.getId()).isEqualTo(savedDto.getMemberId());
  }

  void findById() {
    Long savedDiaryId = savedDto.getId();
    DiaryDto findDto = service.findById(savedDiaryId);

    assertThat(savedDto).isEqualTo(findDto);
  }

  void findByYearMonth() {
    List<DiaryDto> diariesByMonth = service.findDiariesByMonth(writerDto.getId(), DateUtil.getYearMonth(now));

    assertThat(diariesByMonth).hasSize(1);
    assertThat(savedDto).isEqualTo(diariesByMonth.get(0));
  }

  void update() {
    LocalDate updatedDate = now.minusMonths(1);
    DiaryDto requestUpdateDto = DiaryDto.builder()
        .id(savedDto.getId())
        .subject("updated subject")
        .content("updated content")
        .emotionStatus(EmotionStatus.VERY_BAD)
        .diaryDate(updatedDate)
        .diaryYearMonth(DateUtil.getYearMonth(updatedDate))
        .memberId(savedDto.getMemberId())
        .build();

    DiaryDto updatedDto = service.update(requestUpdateDto);

    assertThat(requestUpdateDto).isEqualTo(updatedDto);
    assertThat(savedDto).isNotEqualTo(updatedDto);
  }

  void delete() {
    service.deleteById(savedDto.getId());

    assertThatThrownBy(() -> service.findById(savedDto.getId()))
        .isInstanceOf(NoSuchElementException.class);
  }
}