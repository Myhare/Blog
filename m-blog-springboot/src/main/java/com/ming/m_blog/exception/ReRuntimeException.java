package com.ming.m_blog.exception;

import com.ming.m_blog.enums.StatusCodeEnum;
import lombok.*;

@Getter
@AllArgsConstructor
public class ReRuntimeException extends RuntimeException{

    // 错误码
    public int code = StatusCodeEnum.FAIL.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public ReRuntimeException(String message){
        this.message = message;
    }

}
