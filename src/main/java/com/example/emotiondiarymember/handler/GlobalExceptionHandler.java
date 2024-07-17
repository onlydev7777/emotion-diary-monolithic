package com.example.emotiondiarymember.handler;

import static com.example.emotiondiarymember.error.ApiResult.ERROR;

import com.example.emotiondiarymember.error.code.ErrorCode;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private ResponseEntity<?> newResponse(Throwable throwable, HttpStatus status) {
    return newResponse(throwable, status, null);
  }

  /**
   * @param throwable       에러 객체
   * @param status          http status 값으로 도 사용됨
   * @param customErrorCode 에러 코드
   * @return ResponseEntity
   */
  private ResponseEntity<?> newResponse(Throwable throwable, HttpStatus status, String customErrorCode) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    return new ResponseEntity<>(ERROR(throwable, status, customErrorCode),
        headers, status);
  }

  /**
   * @param status    http status 값으로 도 사용됨
   * @param errorCode 정의된 ERROR CODE
   * @return ResponseEntity
   */
  private ResponseEntity<?> newResponse(HttpStatus status, ErrorCode errorCode) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    return new ResponseEntity<>(ERROR(errorCode.getMessage(), status, errorCode.getErrorCode()),
        headers, status);
  }

  //@Valid 조건 오류
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.warn("handleMethodArgumentNotValidException: {}", e.getMessage(), e);
    return newResponse(e, HttpStatus.BAD_REQUEST);
  }

  //404 NOT_FOUND
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
    log.error("handleNoHandlerFoundException: {}", e.getMessage(), e);
    return newResponse(e, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({
      IllegalStateException.class, IllegalArgumentException.class,
      TypeMismatchException.class, HttpMessageNotReadableException.class,
      MissingServletRequestParameterException.class, MultipartException.class
  })
  public ResponseEntity<?> handleBadRequestException(Exception e) {
    log.error("handleBadRequestException: {}", e.getMessage(), e);
    return newResponse(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<?> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage(), e);
    return newResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(HttpMediaTypeException.class)
  public ResponseEntity<?> handleHttpMediaTypeException(HttpMediaTypeException e) {
    log.warn("handleHttpMediaTypeException: {}", e.getMessage(), e);
    return newResponse(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  /* Security Exception */
  @ExceptionHandler({AuthenticationException.class, JwtException.class})
  public ResponseEntity<?> handleAuthenticationException(RuntimeException e) {
    log.error("handleAuthenticationException: {}", e.getMessage(), e);
    return newResponse(e, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthorizationServiceException.class)
  public ResponseEntity<?> handleAuthorizationException(AuthorizationServiceException e) {
    log.error("AuthorizationServiceException: {}", e.getMessage(), e);
    return newResponse(e, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
    log.error("AccessDeniedException: {}", e.getMessage(), e);
    return newResponse(e, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({Exception.class, RuntimeException.class})
  public ResponseEntity<?> handleException(Exception e) {
    log.error("handleException: {}", e.getMessage(), e);
    return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
