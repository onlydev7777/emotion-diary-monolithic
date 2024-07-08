package com.example.emotiondiarymember.repository;

import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByUserIdAndSocialType(String userId, SocialType socialType);
}
