package com.example.emotiondiarymember.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2Controller {

  @GetMapping("/index")
  public String index() {
    return "index";
  }
}
