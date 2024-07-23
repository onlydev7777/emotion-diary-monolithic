package com.example.emotiondiarymember.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

  private final RedisTemplate<String, Object> template;

  public void save(String key, Object value, long timeout) {
    template.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
  }

  public Optional<Object> get(String key) {
    return Optional.ofNullable(template.opsForValue().get(key));
  }

  public void delete(String key) {
    template.delete(key);
  }
}
