package com.ff.sxbank.service;

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-20
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {
    ResponseResult getOrders(HttpServletRequest request);
}
