package com.example.emotiondiarymember;

import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.entity.embeddable.Email;
import com.example.emotiondiarymember.entity.embeddable.Password;
import com.example.emotiondiarymember.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InitData {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  public void init() {
    memberRepository.save(Member.of(
        "test",
        new Password("qwer1234!", passwordEncoder),
        "테스트",
        new Email("test@test.com"),
        SocialType.NONE)
    );
  }
}
