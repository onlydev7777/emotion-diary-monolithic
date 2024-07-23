package com.example.emotiondiarymember.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmotionStatus {
  GREAT("1"), GOOD("2"), NORMAL("3"), BAD("4"), VERY_BAD("5");

  @JsonValue
  private final String id;
}
