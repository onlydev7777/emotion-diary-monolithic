package com.example.emotiondiarymember.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CookieUtil {

  /**
   * 쿠키 생성
   *
   * @param name     쿠키 이름
   * @param value    쿠키 값
   * @param maxAge   쿠키의 유효 기간 (초 단위)
   * @param httpOnly HttpOnly 속성 설정 여부
   * @param secure   Secure 속성 설정 여부
   * @param path     쿠키 경로
   * @return 생성된 쿠키
   */
  public static Cookie createCookie(String name, String value, int maxAge, boolean httpOnly, boolean secure, String path) {
    Cookie cookie = new Cookie(name, URLEncoder.encode(value, StandardCharsets.UTF_8));
    cookie.setMaxAge(maxAge);
    cookie.setHttpOnly(httpOnly);
    cookie.setSecure(secure);
    cookie.setPath(path);
    return cookie;
  }

  /**
   * 쿠키를 응답에 추가
   *
   * @param response HttpServletResponse
   * @param cookie   추가할 쿠키
   */
  public static void addCookie(HttpServletResponse response, Cookie... cookies) {
    for (Cookie cookie : cookies) {
      response.addCookie(cookie);
    }
  }

  /**
   * 쿠키 조회
   *
   * @param request HttpServletRequest
   * @param name    쿠키 이름
   * @return 해당 이름의 쿠키 value
   */
  public static String getCookieValue(HttpServletRequest request, String name) {
    if (request.getCookies() == null) {
      return null;
    }
    return URLDecoder.decode(
        Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(name))
            .findFirst()
            .orElseThrow()
            .getValue(),
        StandardCharsets.UTF_8
    );
  }

  /**
   * 주어진 이름의 쿠키를 삭제합니다.
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @param names    삭제할 쿠키의 이름들
   */
  public static void deleteCookies(HttpServletRequest request, HttpServletResponse response, String... names) {
    if (request.getCookies() != null) {
      Arrays.stream(names).forEach(name -> {
        Optional<Cookie> cookieOptional = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(name))
            .findFirst();

        cookieOptional.ifPresent(cookie -> {
          cookie.setValue(null);
          cookie.setPath("/");
          cookie.setMaxAge(0);
          response.addCookie(cookie);
        });
      });
    }
  }

  /**
   * "Set-Cookie" 헤더에서 특정 쿠키 이름에 해당하는 쿠키 객체를 반환합니다.
   *
   * @param cookieHeaders "Set-Cookie" 헤더 값 리스트
   * @param cookieName      찾고자 하는 쿠키의 이름
   * @return 쿠키 객체 (Optional)
   */
  public static Optional<Cookie> getCookieFromCookieHeader(List<String> cookieHeaders, String cookieName) {
    if (cookieHeaders == null || cookieName == null) {
      return Optional.empty();
    }

    for (String cookieHeader : cookieHeaders) {
      if (cookieHeader.startsWith(cookieName + "=")) {
        // 쿠키 이름과 값을 추출
        String[] cookieParts = cookieHeader.split(";");
        String[] nameValuePair = cookieParts[0].split("=", 2);
        String name = nameValuePair[0];
        String value = URLDecoder.decode(nameValuePair[1], StandardCharsets.UTF_8);

        // Cookie 객체 생성
        Cookie cookie = new Cookie(name, value);

        // 쿠키의 속성을 추가로 설정 (path, domain, max-age 등)
        for (int i = 1; i < cookieParts.length; i++) {
          String part = cookieParts[i].trim();
          if (part.startsWith("Path=")) {
            cookie.setPath(part.substring("Path=".length()));
          } else if (part.startsWith("Domain=")) {
            cookie.setDomain(part.substring("Domain=".length()));
          } else if (part.startsWith("Max-Age=")) {
            cookie.setMaxAge(Integer.parseInt(part.substring("Max-Age=".length())));
          } else if (part.equals("HttpOnly")) {
            cookie.setHttpOnly(true);
          } else if (part.equals("Secure")) {
            cookie.setSecure(true);
          }
        }

        return Optional.of(cookie);
      }
    }
    return Optional.empty();
  }

  /**
   * 쿠키의 이름, 값을 string value 로 변환합니다.
   *
   * @param cookie 쿠키 객체
   * @return cookie to string value
   */
  public static String cookieToString(Cookie cookie) {
    if (cookie == null) {
      return null;
    }

    // 쿠키 문자열 생성
    return cookie.getName() + "=" + URLEncoder.encode(cookie.getValue(), StandardCharsets.UTF_8);
  }

  /**
   * @param cookies  추가할 쿠키 객체
   * @return HttpHeader 에 들어갈 쿠키 List<String>
   */
  public static List<String> cookiesToList(Cookie... cookies) {
    if (cookies == null) {
      return null;
    }

    List<String> result = new ArrayList<>();

    for (Cookie cookie : cookies) {
      result.add(cookieToString(cookie));
    }

    return result;
  }
}
