package com.ff.sxbank.interceptor;

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 功能描述:
 * @Param:
 * @return:
 * @Author: 徐立峰
 * 时间: 2/5/2022 3:50 PM
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        if (token == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(ResponseResult.error("没有携带token"));
            return false;
        }
        //获取请求头中令牌
        try {
            JWTUtils.verify(token);//验证令牌
            //log.getInfo("令牌解压成功");
            return true;//放行请求
        } catch (Exception e) {
            log.info("jwt验证出现异常,msg:{}", e.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(ResponseResult.error(e.getMessage()));
        }
        return false;
    }
}

