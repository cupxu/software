package com.ff.sxbank.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.SeckillOrder;
import com.ff.sxbank.mapper.SeckillOrderMapper;
import com.ff.sxbank.service.ISeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.sxbank.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-20
 */
@Service
@Slf4j
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    SeckillOrder order;
    @Autowired
    public void setOrder(SeckillOrder order) {
        this.order = order;
    }

    SeckillOrderMapper seckillOrderMapper;
    @Autowired
    public void setSeckillOrderMapper(SeckillOrderMapper seckillOrderMapper) {
        this.seckillOrderMapper = seckillOrderMapper;
    }

    @Override
    public ResponseResult getOrders(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            DecodedJWT userInfo = JWTUtils.getDecodedToken(token);
            String identityNumber = userInfo.getClaim("identityNumber").asString();
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("user_identity", identityNumber);
            List<SeckillOrder> list = seckillOrderMapper.selectList(wrapper);
            return ResponseResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("查询订单出现问题：{}",e.getMessage());
            return ResponseResult.error(e.getMessage());
        }
    }
}
