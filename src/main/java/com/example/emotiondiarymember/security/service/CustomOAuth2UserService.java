package com.example.emotiondiarymember.security.service;

import com.example.emotiondiarymember.controller.request.MemberJoinRequest;
import com.example.emotiondiarymember.dto.MemberDto;
import com.example.emotiondiarymember.mapper.MemberMapper;
import com.example.emotiondiarymember.security.dto.MemberDetails;
import com.example.emotiondiarymember.security.dto.oauth.OAuth2Payload;
import com.example.emotiondiarymember.security.dto.oauth.SocialMember;
import com.example.emotiondiarymember.security.jwt.Payload;
import com.example.emotiondiarymember.service.MemberService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberService service;
  private final MemberMapper mapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    ClientRegistration clientRegistration = userRequest.getClientRegistration();
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
    SocialMember socialMember = SocialMember.of(clientRegistration, oAuth2User);

    Optional<MemberDto> findMemberDto = service.findByUserIdAndSocialType(socialMember.getEmail(), socialMember.getSocialType());
    if (findMemberDto.isEmpty()) {
      MemberJoinRequest memberJoinRequest = MemberJoinRequest.of(socialMember);
      //save OAuth2 Member
      MemberDto savedDto = service.save(mapper.toDto(memberJoinRequest, passwordEncoder));
      findMemberDto = Optional.of(savedDto);
    }

    return new OAuth2Payload(socialMember, Payload.of(MemberDetails.of(findMemberDto.get())));
  }
}
