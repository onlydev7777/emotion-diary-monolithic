package com.example.emotiondiarymember.repository;

import com.example.emotiondiarymember.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
