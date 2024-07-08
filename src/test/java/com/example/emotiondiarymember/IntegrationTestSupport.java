package com.example.emotiondiarymember;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTestSupport {

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected Environment env;
}
