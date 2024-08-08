package com.example.emotiondiarymember.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.emotiondiarymember.IntegrationTestSupport;
import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.error.ApiResult;
import com.example.emotiondiarymember.security.authentication.LoginRequest;
import com.example.emotiondiarymember.security.authentication.LoginResponse;
import com.example.emotiondiarymember.security.jwt.Jwt;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import com.example.emotiondiarymember.security.jwt.Payload;
import com.example.emotiondiarymember.util.CookieUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
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
  //  private String refreshToken;
//  private Cookie accessTokenCookie;
  private Cookie refreshTokenCookie;
  //given
  private final static String ID = "test";
  private final static String PASSWORD = "qwer1234!";
  private final static SocialType SOCIAL_TYPE = SocialType.NONE;

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    return Arrays.asList(
        DynamicTest.dynamicTest("/login 요청 후 Access-Token, Refresh-Token 이 정상 발급 된다.", () -> 로그인()),
        DynamicTest.dynamicTest("Access-Token 을 담아서 /auth/test-ok 페이지에 정상 접속 확인한다.", () -> 로그인_후_토큰인증()),
        DynamicTest.dynamicTest("토큰 정보 없이(로그인 없이) '/auth/test-ok' 접속 시 401 오류 발생", () -> 로그인_하지않고_접속시_401_오류())
    );
  }

  @TestFactory
  Collection<DynamicTest> dynamicTests2() {
    return Arrays.asList(
        DynamicTest.dynamicTest("/login 요청 후 Access-Token, Refresh-Token 이 정상 발급 된다.", () -> 로그인()),
        DynamicTest.dynamicTest("/logout 요청이 정상처리 된다.", () -> 로그아웃()),
        DynamicTest.dynamicTest("/logout 요청이 완료 된 후 해당 Access-Token 으로는 접속이 불가하다.", () -> 로그아웃_이후_접속불가())
    );
  }

  @TestFactory
  Collection<DynamicTest> dynamicTests3() {
    return Arrays.asList(
        DynamicTest.dynamicTest("/login 요청 후 Access-Token, Refresh-Token 이 정상 발급 된다.", () -> 로그인()),
        DynamicTest.dynamicTest("Refresh-Token 헤더에 Refresh-Token 을 담아서 /auth/refresh-token 요청 시 새로운 access-token이 발급된다.", () -> 리프레시_토큰으로_새토큰_발급()),
        DynamicTest.dynamicTest("새로 발급받은 Access-Token으로 정상접속 가능하다.", () -> 로그인_후_토큰인증())
    );
  }


  //  @DisplayName("/login 요청 후 Access-Token, Refresh-Token 이 정상 발급 된다.")
  @Test
  void 로그인() {
    LoginRequest request = new LoginRequest(ID, PASSWORD, SOCIAL_TYPE);

    //when
    ResponseEntity<LoginResponse> jwtResponseEntity = restTemplate.postForEntity("/login",
        request, LoginResponse.class);

    //Access-Token : Response Body And Response Header
    LoginResponse loginResponse = jwtResponseEntity.getBody();
    Jwt jwt = loginResponse.getJwt();
    Payload payload = jwtProvider.verifyToken(jwtProvider.getTokenPrefix() + jwt.getAccessToken());
    accessToken = jwt.getAccessToken();

    //Refresh-Token : Cookie
    List<String> cookies = jwtResponseEntity.getHeaders().get("Set-Cookie");
    this.refreshTokenCookie = CookieUtil.getCookieFromCookieHeader(cookies, jwtProvider.getRefreshTokenHeader()).orElseThrow();

    //then
    assertThat(jwtResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(cookies).isNotEmpty();
    assertThat(cookies).hasSize(1);

    assertThat(payload.getUserId()).isEqualTo(ID);
    assertThat(jwt.getRefreshToken()).isEqualTo(jwtProvider.resolveToken(this.refreshTokenCookie.getValue()));

  }

  //  @DisplayName("Access-Token 을 담아서 /auth/test-ok 페이지에 정상 접속 확인한다.")
//  @Test
  void 로그인_후_토큰인증() throws JsonProcessingException {
    //given
    //when
    HttpHeaders headers = new HttpHeaders();
    headers.set(jwtProvider.getAccessTokenHeader(), URLEncoder.encode(jwtProvider.getTokenPrefix() + accessToken, StandardCharsets.UTF_8));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add(HttpHeaders.COOKIE, CookieUtil.cookieToString(refreshTokenCookie));

    ResponseEntity<ApiResult<Payload>> exchange = restTemplate.exchange("/auth/test-ok", HttpMethod.GET, new HttpEntity<String>(headers),
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

  //  @DisplayName("로그인 없이 '/auth/test-ok' 접속 시 401 오류 발생")
//  @Test
  void 로그인_하지않고_접속시_401_오류() {
    //given
    //when
    ResponseEntity<String> ok = restTemplate.getForEntity("/auth/test-ok", String.class);

    //then
    String errorMessage = ok.getBody();
    assertThat(ok.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(errorMessage).isEqualTo("Token is Not Empty!");
  }

  void 로그아웃() {
    //given
    HttpHeaders headers = new HttpHeaders();
    headers.set(jwtProvider.getAccessTokenHeader(), URLEncoder.encode(jwtProvider.getTokenPrefix() + accessToken, StandardCharsets.UTF_8));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add(HttpHeaders.COOKIE, CookieUtil.cookieToString(refreshTokenCookie));

    //when
    ResponseEntity<ApiResult<String>> logoutOk = restTemplate.exchange("/auth/logout", HttpMethod.GET, new HttpEntity<String>(headers),
        new ParameterizedTypeReference<>() {
        });

    //then
    assertThat(logoutOk.getStatusCode()).isEqualTo(HttpStatus.OK);
    ApiResult<String> apiResult = logoutOk.getBody();
    String result = apiResult.getResponse();

    assertThat(apiResult.isSuccess()).isTrue();
    assertThat(apiResult.getError()).isNull();
    assertThat(result).isEqualTo("logout");
  }

  void 로그아웃_이후_접속불가() throws InterruptedException {
    Thread.sleep(300);
    //given

    //when
    //then
    HttpHeaders headers = new HttpHeaders();
    headers.set(jwtProvider.getAccessTokenHeader(), URLEncoder.encode(jwtProvider.getTokenPrefix() + accessToken, StandardCharsets.UTF_8));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add(HttpHeaders.COOKIE, CookieUtil.cookieToString(refreshTokenCookie));

    ResponseEntity<String> exchange = restTemplate.exchange("/auth/test-ok", HttpMethod.GET, new HttpEntity<String>(headers),
        new ParameterizedTypeReference<>() {
        });

    String responseBody = exchange.getBody();

    assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(responseBody).isEqualTo("Token is blacklisted");
  }

  void 리프레시_토큰으로_새토큰_발급() throws InterruptedException {
    Thread.sleep(300);
    //given
    System.out.println("prev-accessToken = " + this.accessToken);
    System.out.println("prev-refreshToken = " + this.refreshTokenCookie.getValue());

    //when
    //then
    HttpHeaders headers = new HttpHeaders();
    headers.set(jwtProvider.getAccessTokenHeader(), URLEncoder.encode(jwtProvider.getTokenPrefix() + accessToken, StandardCharsets.UTF_8));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add(HttpHeaders.COOKIE, CookieUtil.cookieToString(refreshTokenCookie));

    ResponseEntity<LoginResponse> exchange = restTemplate.exchange("/auth/refresh-token", HttpMethod.POST, new HttpEntity<String>(headers),
        new ParameterizedTypeReference<>() {
        });

    assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);

    LoginResponse loginResponse = exchange.getBody();
    Jwt jwt = loginResponse.getJwt();
    this.accessToken = jwt.getAccessToken();

    List<String> cookies = exchange.getHeaders().get("Set-Cookie");
    this.refreshTokenCookie = CookieUtil.getCookieFromCookieHeader(cookies, jwtProvider.getRefreshTokenHeader()).orElseThrow();

    Payload payload = jwtProvider.verifyToken(this.accessToken);
    assertThat(payload.getUserId()).isEqualTo(ID);
    assertThat(jwt.getRefreshToken()).isEqualTo(jwtProvider.resolveToken(this.refreshTokenCookie.getValue()));

    System.out.println("after-accessToken = " + this.accessToken);
    System.out.println("after-refreshToken = " + this.refreshTokenCookie.getValue());
  }
}
