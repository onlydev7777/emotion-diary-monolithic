package com.example.emotiondiarymember;

import com.example.emotiondiarymember.constant.EmotionStatus;
import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.entity.Diary;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.entity.auth.Resources;
import com.example.emotiondiarymember.entity.auth.Role;
import com.example.emotiondiarymember.entity.embeddable.Email;
import com.example.emotiondiarymember.entity.embeddable.Password;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Data
@Component
public class TestComponent {

  private Member member;
  private Member member2;
  private Resources resources;
  private Resources resources2;
  private Role role;
  private Role role2;
  private Diary diary;
  private Diary diary2;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public void defaultSetUpMember() {
    Member member1 = Member.of("test-1", new Password("qwer1234!", passwordEncoder), "테스트1", new Email("test1@google.com"), SocialType.NONE);
    Member member2 = Member.of("test-2", new Password("qwer1234!", passwordEncoder), "테스트2", new Email("test2@google.com"), SocialType.NONE);

    setMember(member1);
    setMember2(member2);
  }

  public void defaultSetUpResources() {
    Resources resources = Resources.of("사용자 조회", HttpMethod.GET);
    Resources resources2 = Resources.of("관리자 조회", HttpMethod.GET);

    setResources(resources);
    setResources(resources2);
  }

  public void defaultSetUpDiary() {
    role = Role.of("사용자", "사용자 권한");
    role2 = Role.of("관리자", "관리자 권한");

    setRole(role);
    setRole(role2);
  }

  public void defaultSetUpDiary(Member writer, LocalDate date) {
    diary = Diary.of("제목1", "내용1", EmotionStatus.NORMAL, date, writer);
    diary2 = Diary.of("제목2", "내용2", EmotionStatus.GREAT, date, writer);

    setDiary(diary);
    setDiary2(diary2);
  }
}
