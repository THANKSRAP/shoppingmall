package com.example.shoppingmall.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
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
     * 일반적인 예외 처리
     */
    @ExceptionHandler(Exception.class)
    @SuppressWarnings("unchecked")
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("예상치 못한 예외 발생", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body((ApiResponse<Object>) ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "시스템 오류가 발생했습니다."));
    }

    /**
     * 뷰 렌더링 중 발생하는 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleViewException(Exception e, HttpServletRequest request) {
        log.error("뷰 렌더링 중 예외 발생: {}", request.getRequestURI(), e);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/error");
        mav.addObject("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
        mav.addObject("errorCode", "500");
        return mav;
    }
}