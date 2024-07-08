package com.example.emotiondiarymember.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Email {

  public static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(\\S+)$");

  @Column(name = "email", nullable = false, length = 100)
  private String email;

  public Email(String email) {
    validateEmail(email);
    this.email = email;
  }

  private void validateEmail(String email) {
    Matcher matcher = EMAIL_PATTERN.matcher(email);
    if (!matcher.find()) {
      throw new IllegalArgumentException("유효하지 않은 이메일 주소 입니다.");
    }
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Email email1)) {
      return false;
    }
    return Objects.equals(getEmail(), email1.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getEmail());
  }
}
