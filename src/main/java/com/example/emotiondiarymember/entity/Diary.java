package com.example.emotiondiarymember.entity;

import com.example.emotiondiarymember.constant.EmotionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diary")
@Entity
public class Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "subject", nullable = false, length = 300)
  private String subject;
  @Column(name = "content", nullable = false, length = 5000)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(name = "emotion_status", nullable = false)
  private EmotionStatus emotionStatus;

  @Column(name = "diary_date", nullable = false)
  private LocalDate diaryDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;


  @Builder
  public Diary(String subject, String content, EmotionStatus emotionStatus, LocalDate diaryDate, Member member) {
    this.subject = subject;
    this.content = content;
    this.emotionStatus = emotionStatus;
    this.diaryDate = diaryDate;
    this.member = member;
  }

  public static Diary of(String subject, String content, EmotionStatus emotionStatus, LocalDate diaryDate, Member member) {
    return Diary.builder()
        .subject(subject)
        .content(content)
        .emotionStatus(emotionStatus)
        .diaryDate(diaryDate)
        .member(member)
        .build();
  }
}
