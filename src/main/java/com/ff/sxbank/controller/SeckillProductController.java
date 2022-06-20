package com.ff.sxbank.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.redis.iplimit.AccessLimit;
import com.ff.sxbank.service.impl.SeckillProductServiceImpl;
import com.ff.sxbank.util.JWTUtils;
import com.ff.sxbank.util.LinkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 前端控制器
 * </p>
 * @author xulifeng
 * @since 2022-03-01
 */
@Slf4j
@RestController
@RequestMapping("/seckillProduct")
public class SeckillProductController {

    SeckillProductServiceImpl seckillProductService;

    @Autowired
    public void setSeckillProductService(SeckillProductServiceImpl seckillProductService) {
        this.seckillProductService = seckillProductService;
    }




    @AccessLimit(maxCount = 6,second = 30)
    @RequestMapping("/{path}/doSeckill")
    @Scope("prototype")
    public CompletableFuture<ResponseResult> doSeckill(@RequestBody Map<String, String> map, @PathVariable String path, HttpServletRequest request) {

            //方便测试 把身份证号放进参数 不在token
            String token = request.getHeader("Authorization");
            DecodedJWT userInfo = JWTUtils.getDecodedToken(token);
            //拿到用户的身份证号
            String identityNumber = userInfo.getClaim("identityNumber").asString();
            String productId = map.get("productId");
            // 校验链接
            log.info("校验链接,{}",productId + identityNumber);
            if (!LinkUtil.verifyLink(path, productId + identityNumber)) {
                return CompletableFuture.completedFuture(ResponseResult.error("您不能用此链接参与秒杀此商品"));
            }
            // 执行秒杀
            return seckillProductService.doSeckill(identityNumber, productId);

    }

    @PostMapping("/testSeckill")
    @Scope("prototype")
    public CompletableFuture<ResponseResult> testSeckill(@RequestBody Map<String, String> map) {
        try {
            //拿到用户的身份证号
            String productId = map.get("productId");
            String identityNumber = map.get("identityNumber");
            // 执行秒杀
            return seckillProductService.doSeckill(identityNumber, productId);
        }catch (Exception e) {
            e.printStackTrace();
            log.info("秒杀出现问题:{}",e.getMessage());
            return CompletableFuture.completedFuture(ResponseResult.error(e.getMessage()));
        }
    }

    /**
     * @Description: 获取秒杀产品
     * @Param:
     * @return:
     * @Author: xulifeng
     * @Date: 3/9/2022
     */
    @GetMapping("/getSecKillProducts")
    public ResponseResult getAllSecKillProducts() {
        return seckillProductService.getAllSecKillProducts();
    }

    @PostMapping("/update")
    public ResponseResult updateProduct(@RequestBody Map<String, String> product) {
        log.info("接收到的参数：{}", product);
        return seckillProductService.updateSecKillProduct(product);
    }

    @PostMapping("/delete")
    public ResponseResult deleteProduct(@RequestBody Map<String, String> product) {
        return seckillProductService.deleteSecKillProduct(product.get("seckillProductId"));
    }

    @PostMapping("/insert")
    public ResponseResult insertProduct(@RequestBody Map<String, String> product) {
        log.info("接收到的参数：{}", product);
        return seckillProductService.insertSecKillProduct(product);
    }

    @GetMapping("/getLink")
    public ResponseResult getLink(HttpServletRequest request, @RequestParam String id) {
        String link = seckillProductService.generateLink(request,id);
        return ResponseResult.success("200","成功获取秒杀链接", link);
    }

    @GetMapping("/test")
    public ResponseResult test(HttpServletRequest request) {
        //从token获取用户的信息
        String token = request.getHeader("token");
        log.info("获取到用户token:{}", token);
        try {
            DecodedJWT userInfo = JWTUtils.getDecodedToken(token);
            //拿到用户的身份证号
            String identityNumber = userInfo.getClaim("identityNumber").asString();
            String username = userInfo.getClaim("username").asString();
            log.info("用户身份证号：{}", identityNumber + username);
            return ResponseResult.success(identityNumber);
        } catch (Exception e) {
            log.info("解密异常");
            return ResponseResult.error(e.getMessage());
        }
    }
}
