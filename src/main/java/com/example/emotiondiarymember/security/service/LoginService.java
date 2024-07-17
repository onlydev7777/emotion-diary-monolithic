package com.example.emotiondiarymember.security.service;

import com.example.emotiondiarymember.constant.SocialType;
import com.example.emotiondiarymember.repository.MemberRepository;
import com.example.emotiondiarymember.security.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {

  private final MemberRepository repository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    String id = username.split("\\^")[0];
    SocialType type = SocialType.valueOf(username.split("\\^")[1]);

    return MemberDetails.of(
        repository.findByUserIdAndSocialType(id, type).orElseThrow(
            () -> new UsernameNotFoundException(id + " Not Found")
        )
    );
  }
}
