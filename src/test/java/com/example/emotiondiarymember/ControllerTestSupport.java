package com.example.emotiondiarymember;

import com.example.emotiondiarymember.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(SecurityConfig.class)
public abstract class ControllerTestSupport {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext context;

  @MockBean
  protected AuthenticationManager authenticationManager;

  @Autowired
  protected ObjectMapper objectMapper;
}
