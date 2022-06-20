package com.ff.sxbank.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.service.impl.SeckillOrderServiceImpl;
import com.ff.sxbank.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-20
 */
@RestController
@RequestMapping("/seckill_order")
public class SeckillOrderController {

    SeckillOrderServiceImpl seckillOrderService;
    @Autowired
    public void setSeckillOrderService(SeckillOrderServiceImpl seckillOrderService) {
        this.seckillOrderService = seckillOrderService;
    }

    @GetMapping("/order")
    public ResponseResult getOrders(HttpServletRequest request){

        return seckillOrderService.getOrders(request);
    }
}
