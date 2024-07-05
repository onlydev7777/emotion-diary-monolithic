package com.example.emotiondiarymember.repository;

import com.example.emotiondiarymember.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
