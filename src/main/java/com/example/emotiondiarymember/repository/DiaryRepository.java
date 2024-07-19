package com.example.emotiondiarymember.repository;

import com.example.emotiondiarymember.entity.Diary;
import com.example.emotiondiarymember.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

  List<Diary> findAllByMemberAndDiaryYearMonth(Member writer, String diaryYearMonth);
}
