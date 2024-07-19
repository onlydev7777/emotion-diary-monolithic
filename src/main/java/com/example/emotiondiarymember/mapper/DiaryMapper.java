package com.example.emotiondiarymember.mapper;

import com.example.emotiondiarymember.controller.request.DiaryRequest;
import com.example.emotiondiarymember.controller.request.DiaryUpdateRequest;
import com.example.emotiondiarymember.controller.response.DiaryResponse;
import com.example.emotiondiarymember.dto.DiaryDto;
import com.example.emotiondiarymember.entity.Diary;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.util.DateUtil;
import java.time.LocalDate;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiaryMapper {

  @Mapping(target = "memberId", expression = "java(diary.getMember().getId())")
  DiaryDto toDto(Diary diary);

  @Mapping(target = "member", expression = "java(member)")
  Diary toEntity(DiaryDto dto, @Context Member member);

  DiaryResponse toResponse(DiaryDto dto);

  @Mapping(target = "diaryYearMonth", expression = "java(toDiaryYearMonth(request.getDiaryDate()))")
  DiaryDto toDto(DiaryRequest request);

  @Mapping(target = "diaryYearMonth", expression = "java(toDiaryYearMonth(request.getDiaryDate()))")
  DiaryDto toDto(DiaryUpdateRequest request);

  default String toDiaryYearMonth(LocalDate diaryDate) {
    return DateUtil.getYearMonth(diaryDate);
  }
}
