package com.mrzhang.springbootinit.exception;

import com.mrzhang.springbootinit.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author <a href="https://github.com/zhangxyl">程序员小阳</a>
 * @from 
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
