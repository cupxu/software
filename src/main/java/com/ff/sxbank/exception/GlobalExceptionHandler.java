package com.ff.sxbank.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 全局异常处理
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-02-28 17:21
 **/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @Description: 自定义业务处理异常
     * @Param:
     * @return:
     * @Author: xulifeng
     * @Date: 2/28/2022
     */
    @ExceptionHandler(value = CustomizedException.class)
    @ResponseBody
    public ResponseResult BaseExceptionHandler(HttpServletRequest request, CustomizedException exception) {
        return ResponseResult.error(exception.getErrorCode(), exception.getErrorMsg());
    }

    /**
     * @Description: 空指针异常处理
     * @Param:
     * @return:
     * @Author: xulifeng
     * @Date: 2/28/2022
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest request, NullPointerException e) {
        log.info("空指针异常");
        log.info(e.getMessage());
        return ResponseResult.error("空指针异常");
    }

    /**
     * @Description: 处理其他的异常
     * @Param:
     * @return:
     * @Author: xulifeng
     * @Date: 2/28/2022
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest request, Exception exception) {
        //return ResponseResult.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
        log.info("服务器错误:{}",exception.getMessage());
        exception.printStackTrace();
        return ResponseResult.error(exception.getMessage());
    }

}






















