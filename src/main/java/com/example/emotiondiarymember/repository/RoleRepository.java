package com.example.emotiondiarymember.repository;

import com.example.emotiondiarymember.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
