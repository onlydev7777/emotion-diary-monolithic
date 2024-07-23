package com.example.emotiondiarymember.controller;

import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.error.ApiResult;
import com.example.emotiondiarymember.redis.RedisService;
import com.example.emotiondiarymember.repository.MemberRepository;
import com.example.emotiondiarymember.security.authentication.LoginAuthentication;
import com.example.emotiondiarymember.security.authentication.handler.LoginSuccessHandler;
import com.example.emotiondiarymember.security.jwt.Jwt;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import com.example.emotiondiarymember.security.jwt.Payload;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

  private final JwtProvider jwtProvider;
  private final RedisService redisService;
  private final MemberRepository memberRepository;

  @GetMapping("/auth/logout")
  public ResponseEntity<ApiResult<String>> logout(HttpServletRequest request, HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
    if (authentication != null) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
      LoginAuthentication loginAuthentication = (LoginAuthentication) authentication;
      Jwt jwt = loginAuthentication.getJwt();
      redisService.blackListTokenSave(jwt.getAccessToken(), Boolean.TRUE);
    }
    return ResponseEntity.ok(ApiResult.OK("logout"));
  }

  @PostMapping("/auth/refresh-token")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    //1. validate refresh token
    String requestRefreshToken = jwtProvider.resolveToken(request.getHeader(jwtProvider.getRefreshTokenHeader()));
    String redisKey = jwtProvider.verifyRefreshToken(requestRefreshToken);
    String findRefreshToken = redisService.refreshTokenGet(redisKey);
    if (!requestRefreshToken.equals(findRefreshToken)) {
      throw new JwtException("Invalid refresh token");
    }

    //2. find Member
    Long memberId = Long.parseLong(redisKey.split("/")[0]);   //get MemberId
    Member member = memberRepository.findById(memberId)
        .orElseThrow();

    //3. as-is access token to blacklist token
    String requestAccessToken = jwtProvider.resolveToken(request.getHeader(jwtProvider.getHeader()));
    redisService.blackListTokenSave(requestAccessToken, true);

    //4. create new token and refresh token
    Payload payload = new Payload(member.getId(), member.getUserId(), member.getEmail());
    String newAccessToken = jwtProvider.createToken(payload);
    String newRefreshToken = jwtProvider.refreshToken(payload);
    Jwt jwt = new Jwt(newAccessToken, newRefreshToken);

    //5. set new token and refresh token to response header
    LoginAuthentication refreshAuthentication = LoginAuthentication.authenticated(payload, jwt, List.of());
    new LoginSuccessHandler(redisService, jwtProvider).onAuthenticationSuccess(request, response, refreshAuthentication);
  }

  @GetMapping(value = "/test-ok")
  public ResponseEntity<ApiResult<Payload>> testOk() {
    LoginAuthentication authentication = (LoginAuthentication) SecurityContextHolder.getContext().getAuthentication();
    Payload principal = (Payload) authentication.getPrincipal();
    return ResponseEntity.ok(ApiResult.OK(principal));
  }
}
