package com.example.emotiondiarymember.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.emotiondiarymember.IntegrationTestSupport;
import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.error.ApiResult;
import com.example.emotiondiarymember.security.authentication.LoginRequest;
import com.example.emotiondiarymember.security.jwt.Jwt;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import com.example.emotiondiarymember.security.jwt.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class SecurityTest extends IntegrationTestSupport {

  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private JwtProvider jwtProvider;
  private String accessToken;
  private String refreshToken;
  //given
  private final static String ID = "test";
  private final static String PASSWORD = "qwer1234!";
  private final static SocialType SOCIAL_TYPE = SocialType.NONE;

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    return Arrays.asList(
        DynamicTest.dynamicTest("/login 요청 후 Access-Token, Refresh-Token 이 정상 발급 된다.", () -> 로그인_테스트()),
        DynamicTest.dynamicTest("Access-Token 을 담아서 /test-ok 페이지에 정상 접속 확인한다.", () -> 로그인_후_토큰인증()),
        DynamicTest.dynamicTest("토큰 정보 없이(로그인 없이) '/test-ok' 접속 시 401 오류 발생", () -> 로그인_하지않고_접속시_401_오류())
    );
  }

  //  @DisplayName("/login 요청 후 Access-Token, Refresh-Token 이 정상 발급 된다.")
//  @Test
  public void 로그인_테스트() {
    LoginRequest request = new LoginRequest(ID, PASSWORD, SOCIAL_TYPE);

    //when
    ResponseEntity<Jwt> jwtResponseEntity = restTemplate.postForEntity("/login",
        request, Jwt.class);

    //then
    assertThat(jwtResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    Jwt jwt = jwtResponseEntity.getBody();
    Payload payload = jwtProvider.verifyToken(jwtProvider.getTokenPrefix() + jwt.getAccessToken());
    assertThat(payload.getUserId()).isEqualTo(ID);

    accessToken = jwt.getAccessToken();
    refreshToken = jwt.getRefreshToken();
  }

  //  @DisplayName("Access-Token 을 담아서 /test-ok 페이지에 정상 접속 확인한다.")
//  @Test
  void 로그인_후_토큰인증() throws JsonProcessingException {
    //given
    System.out.println("accessToken = " + accessToken);
    System.out.println("refreshToken = " + refreshToken);

    //when
    HttpHeaders headers = new HttpHeaders();
    headers.set(jwtProvider.getHeader(), URLEncoder.encode(jwtProvider.getTokenPrefix() + accessToken, StandardCharsets.UTF_8));
    headers.set(jwtProvider.getRefreshTokenHeader(), URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
    headers.setContentType(MediaType.APPLICATION_JSON);

    ResponseEntity<ApiResult<Payload>> exchange = restTemplate.exchange("/test-ok", HttpMethod.GET, new HttpEntity<String>(headers),
        new ParameterizedTypeReference<>() {
        });

    //then
    assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
    ApiResult<Payload> result = exchange.getBody();
    Payload payload = result.getResponse();

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getError()).isNull();
    assertThat(payload.getUserId()).isEqualTo(ID);
//    assertThat(payload.getRole()).isEqualTo(Role.USER);
  }

  //  @DisplayName("로그인 없이 '/test-ok' 접속 시 401 오류 발생")
//  @Test
  void 로그인_하지않고_접속시_401_오류() {
    //given
    //when
    ResponseEntity<String> ok = restTemplate.getForEntity("/test-ok", String.class);

    //then
    String errorMessage = ok.getBody();
    assertThat(ok.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(errorMessage).isEqualTo("Token is Not Empty!");
  }
}
