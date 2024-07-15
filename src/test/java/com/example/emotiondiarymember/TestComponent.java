package com.example.emotiondiarymember;

import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.entity.embeddable.Email;
import com.example.emotiondiarymember.entity.embeddable.Password;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Data
@Component
public class TestComponent {

  private Member member;
  private Member member2;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public void defaultSetUpMember() {
    Member member1 = Member.of("test-1", new Password("qwer1234!", passwordEncoder), "테스트1", new Email("test1@google.com"), SocialType.NONE);
    Member member2 = Member.of("test-2", new Password("qwer1234!", passwordEncoder), "테스트2", new Email("test2@google.com"), SocialType.NONE);

    setMember(member1);
    setMember2(member2);
  }
}
