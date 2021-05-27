package com.example.lms.exception;

import lombok.Data;

/**
 * @author 王子昂
 * @date 24/5/21
 * @description 异常
 */
@Data
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int code;

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }
}
