package com.example.emotiondiarymember.controller;

import com.example.emotiondiarymember.controller.request.DiaryRequest;
import com.example.emotiondiarymember.controller.request.DiaryUpdateRequest;
import com.example.emotiondiarymember.controller.response.DiaryResponse;
import com.example.emotiondiarymember.error.ApiResult;
import com.example.emotiondiarymember.mapper.DiaryMapper;
import com.example.emotiondiarymember.security.util.TokenUtil;
import com.example.emotiondiarymember.service.DiaryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/diary")
@RestController
public class DiaryController {

  private final DiaryService service;
  private final DiaryMapper mapper;

  @GetMapping("/{diaryId}")
  public ResponseEntity<ApiResult<DiaryResponse>> findDiary(Long diaryId) {
    DiaryResponse diaryResponse = mapper.toResponse(service.findById(diaryId));
    return ResponseEntity.ok(ApiResult.OK(diaryResponse));
  }

  @PostMapping
  public ResponseEntity<ApiResult<DiaryResponse>> saveDiary(@RequestBody DiaryRequest request) {
    DiaryResponse savedDiaryResponse = mapper.toResponse(service.save(mapper.toDto(request)));
    return ResponseEntity.ok(ApiResult.OK(savedDiaryResponse));
  }

  @PatchMapping
  public ResponseEntity<ApiResult<DiaryResponse>> updateDiary(@RequestBody DiaryUpdateRequest request) {
    DiaryResponse savedDiaryResponse = mapper.toResponse(service.update(mapper.toDto(request)));
    return ResponseEntity.ok(ApiResult.OK(savedDiaryResponse));
  }

  @GetMapping("/month-list")
  public ResponseEntity<ApiResult<List<DiaryResponse>>> findDiariesByMonth(@RequestParam String diaryYearMonth) {
    Long memberId = TokenUtil.getMemberId();
    List<DiaryResponse> diaryResponseList = service.findDiariesByMonth(memberId, diaryYearMonth).stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok(ApiResult.OK(diaryResponseList));
  }
}
