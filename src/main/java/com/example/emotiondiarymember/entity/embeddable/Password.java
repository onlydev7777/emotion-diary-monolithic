package com.example.emotiondiarymember.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Password {

  /* 비밀번호 일반형 */
  private static final Pattern PASSWORD_PATTERN_DEFAULT = Pattern.compile(
      "^(?=(?:[^`~!@#$%^*&()\\-_=+]*[`~!@#$%^*&()\\-_=+]){1,})(?=.*\\d)(?=.*[a-zA-Z]).{8,16}$");


  @Column(name = "password", nullable = false, length = 100)
  private String password;

  public Password(String password, PasswordEncoder passwordEncoder) {
    validatePassword(password);
    this.password = passwordEncoder.encode(password);
  }

  private void validatePassword(String password) {
    Matcher matcher = PASSWORD_PATTERN_DEFAULT.matcher(password);
    if (!matcher.find()) {
      throw new IllegalArgumentException("패스워드는 8자 이상 16자 이하 특수문자 1개 이상 포함되어야 합니다.");
    }
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Password password1)) {
      return false;
    }
    return Objects.equals(getPassword(), password1.getPassword());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPassword());
  }
}
