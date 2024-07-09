package com.example.emotiondiarymember.security.dto;

import com.example.emotiondiarymember.entity.Member;
import com.example.emotiondiarymember.entity.embeddable.Email;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class MemberDetails implements UserDetails {

  private Long id;
  private String userId;
  private String password;
  private Email email;
//  private Role role;

  @Override
  public String getUsername() {
    return this.userId;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }


  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public static MemberDetails of(Member member) {
    return new MemberDetails(member.getId(), member.getUserId(), member.getPassword().getPassword(), member.getEmail());
  }
}
