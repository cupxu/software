package com.ff.sxbank.service;

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.SeckillProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-01
 */
public interface ISeckillProductService extends IService<SeckillProduct> {
    CompletableFuture<ResponseResult> doSeckill(String identityNumber, String productId);

    ResponseResult getAllSecKillProducts();

    ResponseResult updateSecKillProduct(Map<String,String> product);

    ResponseResult deleteSecKillProduct(String productId);

    ResponseResult insertSecKillProduct(Map<String,String> product);

    String generateLink(HttpServletRequest request, String productId);
}
