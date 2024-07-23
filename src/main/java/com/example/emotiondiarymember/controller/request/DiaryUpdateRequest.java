package com.example.emotiondiarymember.controller.request;

import com.example.emotiondiarymember.constant.EmotionStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryUpdateRequest {

  private Long id;
  private String subject;
  private String content;
  private EmotionStatus emotionStatus;
  private LocalDate diaryDate;
  private Long memberId;
}
