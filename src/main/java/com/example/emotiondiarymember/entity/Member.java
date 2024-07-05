package com.example.emotiondiarymember.entity;

import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.entity.embeddable.Email;
import com.example.emotiondiarymember.entity.embeddable.Password;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER")
@Entity
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Embedded
  private Password password;

  @Column(name = "name", nullable = false)
  private String name;

  @Embedded
  private Email email;

  @Enumerated(EnumType.STRING)
  private SocialType socialType;
}
