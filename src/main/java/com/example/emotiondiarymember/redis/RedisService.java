package com.example.emotiondiarymember.redis;

import com.example.emotiondiarymember.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisService {

  private static final String ACCESS_TOKEN_PREFIX = "access-token:";
  private static final String BLACKLIST_TOKEN_PREFIX = "blacklist-token:";
  private static final String REFRESH_TOKEN_PREFIX = "refresh-token:";
  private final RedisRepository repository;
  private final JwtProvider jwtProvider;

  public void accessTokenSave(String key, Object value) {
    repository.save(ACCESS_TOKEN_PREFIX + key, value, jwtProvider.getExpirationTime());
  }

  public String accessTokenGet(String key) {
    return (String) repository.get(ACCESS_TOKEN_PREFIX + key)
        .orElseThrow(() -> new RuntimeException("access token not found"));
  }

  public void blackListTokenSave(String key, Object value) {
    repository.save(BLACKLIST_TOKEN_PREFIX + key, value, jwtProvider.getExpirationTime());
  }

  public Boolean blackListTokenGet(String key) {
    return (Boolean) repository.get(BLACKLIST_TOKEN_PREFIX + key)
        .orElse(Boolean.FALSE);
  }

  public void refreshTokenSave(String key, Object value) {
    repository.save(REFRESH_TOKEN_PREFIX + key, value, jwtProvider.getRefreshExpirationTime());
  }

  public String refreshTokenGet(String key) {
    return (String) repository.get(REFRESH_TOKEN_PREFIX + key)
        .orElseThrow(() -> new RuntimeException("refresh token not found"));
  }
}
