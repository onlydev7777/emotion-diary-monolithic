package com.example.emotiondiarymember.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import java.util.ArrayList;
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
    if (throwable instanceof ConstraintViolationException cve) {
      List<FieldError> fieldErrorList = new ArrayList<>();

      cve.getConstraintViolations().forEach(constraintViolation -> {
        StringBuilder fieldBuf = new StringBuilder();

        for (Path.Node node : constraintViolation.getPropertyPath()) {
          if (node.getKind() == ElementKind.PROPERTY || node.getKind() == ElementKind.PARAMETER) {
            if (fieldBuf.isEmpty()) {
              if (node.getIndex() != null) {
                fieldBuf.append("[").append(node.getIndex()).append("] ");
              }
            } else {
              fieldBuf.append(".");
            }

            fieldBuf.append(node.getName());
          }

        }

        fieldErrorList.add(new FieldError(fieldBuf.toString(), null, constraintViolation.getMessage()));
      });

      return new ApiError(throwable.getMessage(), status, errorCode, fieldErrorList);
    } else if (throwable instanceof MethodArgumentNotValidException manve) {
      return new ApiError(throwable.getMessage(), status, errorCode, ofBindingResult(manve.getBindingResult()));
    } else {
      return new ApiError(throwable.getMessage(), status, errorCode, null);
    }

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
