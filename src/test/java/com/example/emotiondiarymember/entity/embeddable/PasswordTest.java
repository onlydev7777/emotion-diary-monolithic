package com.example.emotiondiarymember.entity.embeddable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordTest {

  @DisplayName("특수문자가 없으면 비밀번호 검증 실패!")
  @Test
  void 비밀번호_유효성_검증() {
    //given
    String inputPassword = "qwer1234";
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //when
    //then
    assertThatThrownBy(() -> new Password(inputPassword, encoder))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("8자리 이하이면 비밀번호 검증 실패!")
  @Test
  void 비밀번호_유효성_검증2() {
    //given
    String inputPassword = "qwer12!";
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //when
    //then
    assertThatThrownBy(() -> new Password(inputPassword, encoder))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("8자리 이상 특수문자 1개 이상이면 비밀번호 검증 성공!")
  @Test
  void 비밀번호_유효성_검증_성공() {
    //given
    String inputPassword = "qwer1234!";
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    //when
    Password password = new Password(inputPassword, encoder);
    //then
    assertThat(password.getPassword()).isNotBlank();
  }
}