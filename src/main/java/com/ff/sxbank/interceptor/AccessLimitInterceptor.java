package com.ff.sxbank.interceptor;

/**
 * @description: ip限流拦截器
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-03-16 11:53
 **/

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.redis.iplimit.AccessLimit;
import com.ff.sxbank.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 访问拦截器
 * @Author oyc
 * @Date 2020/10/22 11:20 下午
 */
@Component
@Slf4j
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // Handler 是否为 HandlerMethod 实例
            if (handler instanceof HandlerMethod) {
                // 强转
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                // 获取方法
                Method method = handlerMethod.getMethod();
                // 是否有AccessLimit注解
                if (!method.isAnnotationPresent(AccessLimit.class)) {
                    return true;
                }
                // 获取注解内容信息
                AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
                if (accessLimit == null) {
                    return true;
                }

                int seconds = accessLimit.second();
                int maxCount = accessLimit.maxCount();

                String key = HttpUtil.getIpByRequest(request);
                log.info("ip标识: {}",key);

                // 已经访问的次数
                Integer count = (Integer) redisTemplate.opsForValue().get(key);

                log.info("已经访问的次数: {}", count);

                if ( count == null || count == -1) {
                    redisTemplate.opsForValue().set(key, 1, seconds, TimeUnit.SECONDS);
                    return true;
                }

                if (count < maxCount) {
                    redisTemplate.opsForValue().increment(key);
                    return true;
                }

                if (count >= maxCount) {
                    log.warn("请求过于频繁请稍后再试");
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println(ResponseResult.error("500109","请求过于频繁,请稍后再试"));
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.warn("请求过于频繁请稍后再试");
            e.printStackTrace();
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(ResponseResult.error(e.getMessage()));
        }
        return true;
    }
}
