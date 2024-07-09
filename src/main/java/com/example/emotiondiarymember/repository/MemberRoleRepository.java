package com.example.emotiondiarymember.repository;

import com.example.emotiondiarymember.entity.auth.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

}
