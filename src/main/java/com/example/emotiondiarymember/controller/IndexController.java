package com.example.emotiondiarymember.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @GetMapping("/")
  public Authentication index() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
