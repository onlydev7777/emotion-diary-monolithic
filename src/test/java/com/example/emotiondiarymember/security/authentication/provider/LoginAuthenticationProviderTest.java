package com.example.emotiondiarymember.security.authentication.provider;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.emotiondiarymember.IntegrationTestSupport;
import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.repository.MemberRepository;
import com.example.emotiondiarymember.security.authentication.LoginAuthentication;
import com.example.emotiondiarymember.security.authentication.LoginRequest;
import com.example.emotiondiarymember.security.jwt.Jwt;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import com.example.emotiondiarymember.security.jwt.Payload;
import com.example.emotiondiarymember.security.service.LoginService;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

class LoginAuthenticationProviderTest extends IntegrationTestSupport {

  @Autowired
  private LoginService loginService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtProvider jwtProvider;
  @Autowired
  private MemberRepository memberRepository;

  @DisplayName("로그인 성공 시 authentication.getPrincipal 에 Token 값을 담고 있는 Jwt 객체가 담긴다.")
  @Test
  void 로그인요청_정상() {
    //given
    final String userId = "test";
    final String password = "qwer1234!";
    final SocialType socialType = SocialType.NONE;

    LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider(loginService, passwordEncoder, jwtProvider);
    LoginRequest loginRequest = new LoginRequest(userId, password, socialType);
    LoginAuthentication unauthenticated = LoginAuthentication.unauthenticated(loginRequest);

    //when
    LoginAuthentication authenticate = (LoginAuthentication) loginAuthenticationProvider.authenticate(unauthenticated);

    //then
    List<String> authoritiesNames = authenticate.getAuthorities().stream()
        .map(grantedAuthority -> grantedAuthority.getAuthority())
        .collect(Collectors.toList());

    assertThat(authenticate.getPrincipal()).isInstanceOf(Payload.class);
//    assertThat(authoritiesNames).containsExactly(Role.USER.getAuthority());

    Jwt jwt = authenticate.getJwt();
    Payload payload = jwtProvider.verifyToken(jwt.getAccessToken());
    Member findMember = memberRepository.findByUserIdAndSocialType(userId, SocialType.NONE)
        .orElseThrow();

    assertThat(payload.getUserId()).isEqualTo(loginRequest.getId());
    assertThat(payload.getEmail()).isEqualTo(findMember.getEmail());
    assertThat(payload.getId()).isEqualTo(findMember.getId());
//    assertThat(payload.getRole()).isEqualTo(findMember.getRole());
  }
}
