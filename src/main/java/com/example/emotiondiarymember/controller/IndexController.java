package com.example.emotiondiarymember.controller;

import com.example.emotiondiarymember.error.ApiResult;
import com.example.emotiondiarymember.security.authentication.LoginAuthentication;
import com.example.emotiondiarymember.security.jwt.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class IndexController {


  @GetMapping("/")
  public Authentication index() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @GetMapping(value = "/ok")
  public ResponseEntity<ApiResult<Payload>> ok() {
    LoginAuthentication authentication = (LoginAuthentication) SecurityContextHolder.getContext().getAuthentication();
    Payload principal = (Payload) authentication.getPrincipal();
    return ResponseEntity.ok(ApiResult.OK(principal));
  }
}
