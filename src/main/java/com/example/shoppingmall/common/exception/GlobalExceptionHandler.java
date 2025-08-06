package com.example.shoppingmall.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.example.shoppingmall.common.response.ApiResponse;
import com.example.shoppingmall.user.exception.LoginFailedException;
import com.example.shoppingmall.user.exception.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 예외 처리
     */
    @ExceptionHandler(CustomException.class)
    @SuppressWarnings("unchecked")
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException e) {
        log.error("비즈니스 예외 발생: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body((ApiResponse<Object>) ApiResponse.error(e.getErrorCode().getStatus(), e.getMessage()));
    }

    /**
     * 사용자 관련 예외 처리
     */
    @ExceptionHandler({UserNotFoundException.class, LoginFailedException.class})
    @SuppressWarnings("unchecked")
    public ResponseEntity<ApiResponse<Object>> handleUserException(Exception e) {
        log.warn("사용자 관련 예외 발생: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body((ApiResponse<Object>) ApiResponse.error(ErrorCode.INVALID_REQUEST.getStatus(), e.getMessage()));
    }

    /**
     * 일반적인 런타임 예외 처리
     */
    @ExceptionHandler(RuntimeException.class)
    @SuppressWarnings("unchecked")
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException e) {
        log.error("런타임 예외 발생", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body((ApiResponse<Object>) ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다."));
    }

    /**
     * 일반적인 예외 처리 (응답 타입에 따라 JSON 또는 뷰 반환)
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest request) {
        log.error("예상치 못한 예외 발생", e);

        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE)) {
            // 요청이 JSON 타입을 요구할 경우, JSON 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((ApiResponse<Object>) ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "시스템 오류가 발생했습니다."));
        } else {
            // 뷰 렌더링 (HTML 응답) 반환
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            mav.addObject("errorCode", "500");
            return mav;
        }
    }
}