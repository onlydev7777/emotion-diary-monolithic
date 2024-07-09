package com.example.emotiondiarymember.security.authentication.provider;

import com.example.emotiondiarymember.security.authentication.LoginAuthentication;
import com.example.emotiondiarymember.security.dto.MemberDetails;
import com.example.emotiondiarymember.security.jwt.Jwt;
import com.example.emotiondiarymember.security.jwt.JwtProvider;
import com.example.emotiondiarymember.security.jwt.Payload;
import com.example.emotiondiarymember.security.service.LoginService;
import com.example.emotiondiarymember.security.util.AuthenticationUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

  private final LoginService service;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  /**
   * 1. 미인증 상태의 Authrntication 을 파라미터로 받는다. 2. DB 에서 요청 id와 요청 password를 검증 3. 검증 완료 시 '인증완료' 상태의 Authrntication 을 반환한다. 4. 반환된 Authrntication 은
   * AbstractAuthenticationProcessingFilter 의 successfulAuthentication 메서드 에서 SecurityContext 에 저장된다.
   *
   * @param authentication the authentication request object.
   * @return
   * @throws AuthenticationException
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String idAndSocialType = AuthenticationUtil.toIdAndSocialType(authentication.getPrincipal());
    MemberDetails memberDetails = (MemberDetails) service.loadUserByUsername(idAndSocialType);

    String credentials = (String) authentication.getCredentials();

    if (!passwordEncoder.matches(credentials, memberDetails.getPassword())) {
      throw new BadCredentialsException("Password Not Matches!");
    }

    String accessToken = jwtProvider.createToken(memberDetails.getId(), memberDetails.getUserId(), memberDetails.getEmail());
    String refreshToken = jwtProvider.refreshToken(memberDetails.getId(), memberDetails.getUserId());
    Jwt jwt = new Jwt(accessToken, refreshToken);
    Payload payload = new Payload(memberDetails.getId(), memberDetails.getUserId(), memberDetails.getEmail());

    return LoginAuthentication.authenticated(payload, jwt, List.of());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return LoginAuthentication.class.isAssignableFrom(authentication);
  }
}
