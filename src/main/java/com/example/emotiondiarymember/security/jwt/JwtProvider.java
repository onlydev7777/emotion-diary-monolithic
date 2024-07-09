package com.example.emotiondiarymember.security.jwt;

import com.example.emotiondiarymember.entity.embeddable.Email;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProvider {

  private String header;
  private String refreshTokenHeader;
  private String iss;
  private String secretKey;
  private int expirationTime = 30 * 60 * 1000; // 30분
  private int refreshExpirationTime = 24 * 60 * 60 * 1000; // 하루
  private final static String CLAIMS_KEY = "payload";

  public String createToken(Long id, String userId, Email email) {
    Payload payload = new Payload(id, userId, email);
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    String subject = id + "/" + userId;

    return Jwts.builder()
        .subject(subject)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expirationTime))
        .issuer(iss)
        .signWith(key, SIG.HS512)
        .claim(CLAIMS_KEY, payload)
        .compact();
  }

  public String refreshToken(Long id, String userId) {
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    String subject = id + "/" + userId;

    return Jwts.builder()
        .subject(subject)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
        .issuer(iss)
        .signWith(key, SIG.HS512)
        .compact();
  }

  public Payload verifyToken(String jwtToken) {
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    Jws<Claims> claimsJws = Jwts.parser()
        .json(new JacksonDeserializer(Maps.of(CLAIMS_KEY, Payload.class).build()))
        .verifyWith(key)
        .build()
        .parseSignedClaims(jwtToken);

    return claimsJws.getPayload().get(CLAIMS_KEY, Payload.class);
  }
}
