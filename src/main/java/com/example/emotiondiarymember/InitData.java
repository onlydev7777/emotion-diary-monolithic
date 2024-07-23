package com.example.emotiondiarymember;

import com.example.emotiondiarymember.constant.EmotionStatus;
import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.entity.Diary;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.entity.embeddable.Email;
import com.example.emotiondiarymember.entity.embeddable.Password;
import com.example.emotiondiarymember.repository.DiaryRepository;
import com.example.emotiondiarymember.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InitData {

  private final MemberRepository memberRepository;
  private final DiaryRepository diaryRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  public void init() {
    Member savedMember = memberRepository.save(Member.of(
        "test",
        new Password("qwer1234!", passwordEncoder),
        "테스트",
        new Email("test@test.com"),
        SocialType.NONE)
    );

    diaryRepository.saveAll(
        List.of(Diary.of(
                "테스트 제목1",
                "테스트 내용1",
                EmotionStatus.GREAT,
                LocalDate.now(),
                savedMember
            ),
            Diary.of(
                "테스트 제목2",
                "테스트 내용2",
                EmotionStatus.BAD,
                LocalDate.now(),
                savedMember
            ))
    );
  }
}
