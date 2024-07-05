package com.example.emotiondiarymember.entity.embeddable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {

  @DisplayName("유효하지 않은 이메일 주소 검증 실패!")
  @Test
  void 이메일_유효성_검증() {
    //given
    String inputEmail = "asdzzzz";

    //when
    //then
    assertThatThrownBy(() -> new Email(inputEmail))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("이메일 주소 검증 성공!")
  @Test
  void 이메일_유효성_검증_성공() {
    //given
    String inputEmail = "asdzzzz@gmail.com";

    //when
    Email email = new Email(inputEmail);
    //then
    assertThat(email.getEmail()).isEqualTo(inputEmail);
  }
}