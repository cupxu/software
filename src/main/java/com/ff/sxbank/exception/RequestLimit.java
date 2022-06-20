package com.ff.sxbank.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;


/**
 * 功能描述:
 * @Param:
 * @return:
 * @Author: 徐立峰
 * 时间: 3/15/2022 9:02 PM
 */



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit {
    /**
     * 允许访问的最大次数
     */
    int count() default Integer.MAX_VALUE;

    /**
     * 时间段，单位为毫秒，默认值一分钟
     */
    long time() default 60000;
}
