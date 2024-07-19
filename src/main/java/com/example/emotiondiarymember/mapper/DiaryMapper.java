package com.example.emotiondiarymember.mapper;

import com.example.emotiondiarymember.dto.DiaryDto;
import com.example.emotiondiarymember.entity.Diary;
import com.example.emotiondiarymember.entity.Member;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiaryMapper {

  @Mapping(target = "memberId", expression = "java(diary.getMember().getId())")
  DiaryDto toDto(Diary diary);

  @Mapping(target = "member", expression = "java(member)")
  Diary toEntity(DiaryDto dto, @Context Member member);
}
