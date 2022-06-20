package com.ff.sxbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAsync
@EnableWebMvc
@EnableCaching
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement
public class SxbankApplication {

    public static void main(String[] args) {
        SpringApplication.run(SxbankApplication.class, args);
    }

}
