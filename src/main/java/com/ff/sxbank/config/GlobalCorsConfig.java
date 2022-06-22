package com.ff.sxbank.config;

import com.ff.sxbank.interceptor.AccessLimitInterceptor;
import com.ff.sxbank.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 功能描述:
 * @Param:
 * @return:
 * @Author: 徐立峰
 * 时间: 2/5/2022 10:58 AM
 */
@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer{


    private AccessLimitInterceptor accessLimitInterceptor;
    @Autowired
    public void setAccessLimitInterceptor(AccessLimitInterceptor accessLimitInterceptor) {
        this.accessLimitInterceptor = accessLimitInterceptor;
    }

    private JwtInterceptor jwtInterceptor;
    @Autowired
    public void setJwtInterceptor(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }



    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            //重写父类提供的interceptor接口
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                // 接口限流拦截器
                registry.addInterceptor(accessLimitInterceptor)
                        .excludePathPatterns("/seckillProduct/testSeckill")
                        .addPathPatterns("/seckillProduct/*/doSeckill")
                        .addPathPatterns("/test/ip");

                // registry.addInterceptor(new JwtInterceptor())
                registry.addInterceptor(jwtInterceptor)
                        .addPathPatterns("/**")
                        .excludePathPatterns("/user/**")
                        .excludePathPatterns("/actuator/**")
                        .excludePathPatterns("/admin/**")
                        .excludePathPatterns("/seckillProduct/getSecKillProducts")
                        .excludePathPatterns("/product/add")
                        .excludePathPatterns("/overdueRecord/join")
                        .excludePathPatterns("/seckillProduct/*/doSeckill")
                        .excludePathPatterns("/seckillProduct/testSeckill")
                        .excludePathPatterns("/test/**");
            }


        };
    }
}