package com.example.emotiondiarymember.security.jwt;

import com.example.emotiondiarymember.error.CustomAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProvider {

  private final String accessTokenHeader;
  private final String refreshTokenHeader;
  private final String iss;
  private final String secretKey;
  private final long expirationTime;
  private final String tokenPrefix;
  private final long refreshExpirationTime;
  private final static String CLAIMS_KEY = "payload";

  public JwtProvider(String accessTokenHeader, String refreshTokenHeader, String iss, String secretKey, long expirationTime,
      long refreshExpirationTime,
      String tokenPrefix) {
    this.accessTokenHeader = accessTokenHeader;
    this.refreshTokenHeader = refreshTokenHeader;
    this.iss = iss;
    this.secretKey = secretKey;
    this.expirationTime = expirationTime * 1000;    // 설정 파일에서 seconds 단위로 입력 받음
    this.refreshExpirationTime = refreshExpirationTime * 1000;  // 설정 파일에서 seconds 단위로 입력 받음
    this.tokenPrefix = tokenPrefix == null ? "Bearer " : tokenPrefix;
  }

  public String createToken(Payload payload) {
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    String subject = payload.getRedisKey();

    return Jwts.builder()
        .subject(subject)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expirationTime))
        .issuer(iss)
        .signWith(key, SIG.HS512)
        .claim(CLAIMS_KEY, payload)
        .compact();
  }

  public String refreshToken(String subject) {
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    return Jwts.builder()
        .subject(subject)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
        .issuer(iss)
        .signWith(key, SIG.HS512)
        .compact();
  }

  public Payload verifyToken(String jwtToken) {
    jwtToken = resolveToken(jwtToken);

    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    Jws<Claims> claimsJws = Jwts.parser()
        .json(new JacksonDeserializer(Maps.of(CLAIMS_KEY, Payload.class).build()))
        .verifyWith(key)
        .build()
        .parseSignedClaims(jwtToken);

    return claimsJws.getPayload().get(CLAIMS_KEY, Payload.class);
  }

  public String verifyRefreshToken(String refreshToken) {
    refreshToken = resolveToken(refreshToken);

    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(refreshToken)
        .getPayload()
        .getSubject();
  }

  public String resolveToken(String token) {
    if (token == null) {
      throw new CustomAuthenticationException("Token is Not Empty!");
    }

    String decodedToken = URLDecoder.decode(token, StandardCharsets.UTF_8);
    if (decodedToken.startsWith(tokenPrefix)) {
      return decodedToken.substring(tokenPrefix.length());
    }

    return decodedToken;
  }
}
