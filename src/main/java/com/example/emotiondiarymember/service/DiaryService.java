package com.example.emotiondiarymember.service;

import com.example.emotiondiarymember.dto.DiaryDto;
import com.example.emotiondiarymember.entity.Diary;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.mapper.DiaryMapper;
import com.example.emotiondiarymember.repository.DiaryRepository;
import com.example.emotiondiarymember.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DiaryService {

  private final DiaryRepository repository;
  private final MemberRepository memberRepository;
  private final DiaryMapper mapper;

  @Transactional
  public DiaryDto save(DiaryDto dto) {
    Member member = memberRepository.getReferenceById(dto.getMemberId());
    if (member == null) {
      throw new IllegalArgumentException("Member not found: " + dto.getMemberId());
    }

    Diary savedDiary = repository.save(mapper.toEntity(dto, member));
    return mapper.toDto(savedDiary);
  }

  public DiaryDto findById(Long diaryId) {
    Diary diary = repository.findById(diaryId)
        .orElseThrow();

    return mapper.toDto(diary);
  }

  public List<DiaryDto> findDiariesByMonth(Long memberId, String diaryYearMonth) {
    Member writer = memberRepository.getReferenceById(memberId);
    return repository.findAllByMemberAndDiaryYearMonth(writer, diaryYearMonth).stream()
        .map(mapper::toDto)
        .toList();
  }

  @Transactional
  public DiaryDto update(DiaryDto dto) {
    Diary findDiary = repository.findById(dto.getId())
        .orElseThrow();

    findDiary.update(dto.getSubject(), dto.getContent(), dto.getEmotionStatus(), dto.getDiaryDate());

    return mapper.toDto(findDiary);
  }

  @Transactional
  public void deleteById(Long diaryId) {
    Diary diary = repository.findById(diaryId)
        .orElseThrow();
    repository.delete(diary);
  }
}
