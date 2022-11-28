package com.ming.m_blog.hander;

import com.ming.m_blog.enums.StatusCodeEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.vo.ResponseResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;


/**
 * 全局异常处理
 *
 **/
@Log4j2
@RestControllerAdvice
public class ControllerAdviceHandler {

    /**
     * 处理服务异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = ReRuntimeException.class)
    public ResponseResult<?> errorHandler(ReRuntimeException e) {
        return ResponseResult.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> errorHandler(MethodArgumentNotValidException e) {
        return ResponseResult.fail(StatusCodeEnum.VALID_ERROR.getCode(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult<?> errorHandler(Exception e) {
        e.printStackTrace();
        return ResponseResult.fail(StatusCodeEnum.SYSTEM_ERROR.getCode(), StatusCodeEnum.SYSTEM_ERROR.getDesc());
    }

    /**
     * 处理接口权限异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseResult<?> errorHandler(AccessDeniedException e) {
        e.printStackTrace();
        return ResponseResult.fail(StatusCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 如果在登录认证的UserDetailsServer中抛出异常，会被SpringSecurity捕获并转化成其他异常
     * SpringSecurity如果匹配不到具体的异常，就会把此时的Exception转化成InternalAuthenticationServiceException异常再抛出
     *
     * 这里用来检测前台自定义登录接口认证中自己抛出的认证异常
     *
     * @param e 异常
     * @return  接口异常信息
     */
    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseResult<?> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        return ResponseResult.fail(StatusCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

}
