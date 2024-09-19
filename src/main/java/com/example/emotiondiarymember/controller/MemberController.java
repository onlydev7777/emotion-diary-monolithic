package com.example.emotiondiarymember.controller;

import com.example.emotiondiarymember.controller.request.MemberJoinRequest;
import com.example.emotiondiarymember.controller.response.MemberResponse;
import com.example.emotiondiarymember.dto.MemberDto;
import com.example.emotiondiarymember.error.ApiResult;
import com.example.emotiondiarymember.mapper.MemberMapper;
import com.example.emotiondiarymember.security.authentication.LoginAuthentication;
import com.example.emotiondiarymember.security.jwt.Payload;
import com.example.emotiondiarymember.security.util.TokenUtil;
import com.example.emotiondiarymember.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

  private final MemberService service;
  private final MemberMapper mapper;
  private final PasswordEncoder passwordEncoder;

  @GetMapping
  public ResponseEntity<ApiResult<MemberResponse>> findMember() {
    Long memberId = TokenUtil.getMemberId();
    MemberDto memberDto = service.findById(memberId);
    return ResponseEntity.ok(ApiResult.OK(mapper.toResponse(memberDto)));
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResult<MemberResponse>> joinMember(@RequestBody MemberJoinRequest request) {
    MemberDto savedMemberDto = service.save(mapper.toDto(request, passwordEncoder));
    return ResponseEntity.ok(ApiResult.OK(mapper.toResponse(savedMemberDto)));
  }

  @GetMapping(value = "/test-ok")
  public ResponseEntity<ApiResult<Payload>> testOk() {
    LoginAuthentication authentication = (LoginAuthentication) SecurityContextHolder.getContext().getAuthentication();
    Payload principal = (Payload) authentication.getPrincipal();
    return ResponseEntity.ok(ApiResult.OK(principal));
  }
}
