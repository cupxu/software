package com.ff.sxbank.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.mapper.AdminMapper;
import com.ff.sxbank.mapper.SeckillOrderMapper;
import com.ff.sxbank.mapper.SeckillProductMapper;
import com.ff.sxbank.mapper.UserMapper;
import com.ff.sxbank.pojo.Admin;
import com.ff.sxbank.pojo.SeckillProduct;
import com.ff.sxbank.pojo.User;
import com.ff.sxbank.redis.iplimit.AccessLimit;
import com.ff.sxbank.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

import java.util.List;



/**
 * @description: 测试
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-03-16 12:05
 **/
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    private static final String productId = "1";

    public User user;
    @Autowired
    public void setUser(User user) {
        this.user = user;
    }

    UserServiceImpl userService;
    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    SeckillOrderMapper orderMapper;
    @Autowired
    public void setOrderMapper(SeckillOrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    SeckillProductMapper seckillProductMapper;
    @Autowired
    public void setSeckillProductMapper(SeckillProductMapper seckillProductMapper) {
        this.seckillProductMapper = seckillProductMapper;
    }

    AdminMapper adminMapper;
    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    Admin admin;
    @Autowired
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    // 网络联通测试
    @GetMapping("/get")
    public ResponseResult get() throws Exception{
        return ResponseResult.success("success");
    }

    // ip限流测试
    @GetMapping("/ip")
    @AccessLimit(maxCount = 2,second = 180)
    public ResponseResult limit() {
        log.error("Access Limit Test");
        return ResponseResult.success("测试成功");
    }

    // 压测结果
    @GetMapping("/result")
    public ResponseResult test () {
        int leftAmount = seckillProductMapper.selectById(productId).getSeckillProductAmount();
        int sales = (10000 - leftAmount)*10000;
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("admin_name", "admin");
        admin = adminMapper.selectOne(wrapper);
        int money = admin.getCompanyMoney();
        // order表中订单数
        int orderNum = orderMapper.selectOrderNum();
        // user表中成功购买的人数
        int successNum = userMapper.selectSuccessNum();
        // 查询订单表中重复的数据
        int allNum = userMapper.selectAll();
        List<String> list = new ArrayList<>();
        list.add("一共秒杀产品份数:" + String.valueOf(10000 - leftAmount));
        list.add("秒杀后产品剩余数量:" + String.valueOf(leftAmount));
        list.add("销售金额:" + String.valueOf(sales));
        list.add("公司入账:" + String.valueOf(money));
        list.add("order表中订单数:" + String.valueOf(orderNum));
        list.add("user表中成功购买的人数:" + String.valueOf(successNum));
        list.add("user表中用户数:" + String.valueOf(allNum));
        return ResponseResult.success(list);

    }


}

