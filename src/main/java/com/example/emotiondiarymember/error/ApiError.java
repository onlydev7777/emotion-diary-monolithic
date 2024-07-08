package com.example.emotiondiarymember.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiError {

  private String message;
  private int status;
  private String errorCode;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<FieldError> fieldErrors;

  private ApiError(String message, HttpStatus status, String errorCode, List<FieldError> fieldErrors) {
    this.message = message;
    this.status = status.value();
    this.errorCode = errorCode;
    this.fieldErrors = fieldErrors;
  }

  public static ApiError of(String message, HttpStatus status, String errorCode) {
    return new ApiError(message, status, errorCode, null);
  }

  public static ApiError ofThrowable(Throwable throwable, HttpStatus status, String errorCode) {
    if (throwable instanceof MethodArgumentNotValidException manve) {
      return new ApiError(throwable.getMessage(), status, errorCode, ofBindingResult(manve.getBindingResult()));
    }
    
    return new ApiError(throwable.getMessage(), status, errorCode, null);
  }

  private static List<FieldError> ofBindingResult(BindingResult bindingResult) {
    return bindingResult.getFieldErrors().stream()
        .map(error -> new FieldError(
            error.getField(),
            error.getRejectedValue() == null ? null : error.getRejectedValue().toString(),
            error.getDefaultMessage()
        )).toList();
  }

  @Getter
  public static class FieldError {

    private final String field;

    private final String value;

    private final String reason;

    private FieldError(final String field, final String value, final String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }
  }
}
