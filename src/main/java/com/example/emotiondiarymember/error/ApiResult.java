package com.example.emotiondiarymember.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResult<T> {

  private boolean success;
  private T response;
  private ApiError error;

  public ApiResult() {
  }

  private ApiResult(boolean success, T response, ApiError error) {
    this.success = success;
    this.response = response;
    this.error = error;
  }

  public static <T> ApiResult<T> OK(T response) {
    return new ApiResult<>(true, response, null);
  }

  public static ApiResult<?> ERROR(Throwable throwable, HttpStatus status, String customErrorCode) {
    return new ApiResult<>(false, null, ApiError.ofThrowable(throwable, status, customErrorCode));
  }

  public static ApiResult<?> ERROR(String errorMessage, HttpStatus status, String customErrorCode) {
    return new ApiResult<>(false, null, ApiError.of(errorMessage, status, customErrorCode));
  }

//    public static ApiResult<?> ERROR(ErrorCode errorCode, BindingResult bindingResult) {
//        return new ApiResult<>(false, null, ApiError.ofBindingResult(errorCode, bindingResult));
//    }

}

