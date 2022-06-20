package com.ff.sxbank.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @description: ip限流异常
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-03-15 20:13
 **/
public class RequestLimitException extends NestedRuntimeException {

    public RequestLimitException(){
        super("HTTP请求超出设定的限制");
    }

    public RequestLimitException(String msg) {
        super(msg);
    }
}

