package com.ff.sxbank;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.sxbank.mapper.AdminMapper;
import com.ff.sxbank.mapper.SeckillOrderMapper;
import com.ff.sxbank.mapper.SeckillProductMapper;
import com.ff.sxbank.mapper.UserMapper;
import com.ff.sxbank.pojo.Admin;
import com.ff.sxbank.pojo.SeckillProduct;
import com.ff.sxbank.pojo.User;
import com.ff.sxbank.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
class SxbankApplicationTests {

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

    @Test
    // 添加可成功购买产品的用户
    public void insertUser() {
        // 秒杀测试用户数量
        int amount = 20000;
        for (int i = 0; i < amount; i++) {
            user.setUsername("压测"+(i+1));
            user.setPassword("test123456");
            user.setMoney(10000);
            // 身份证号 为了测试方便要连续 且不能相等
            user.setIdentityNumber("1111111111"+(11111111 + i));
            userService.userRegister(user);
        }
    }

    // 删除模拟用户
    @Test
    public void clearUser() {
        userMapper.clear();
    }

    // 清空订单表
    @Test
    public void clearOrder() {
        orderMapper.delete();
    }

    // 产品恢复
    @Test
    public void productRecovery() {
        SeckillProduct seckillProduct = seckillProductMapper.selectById(productId);
        seckillProduct.setSeckillProductAmount(10000);
        seckillProductMapper.updateById(seckillProduct);
    }

    // 清空公司账户
    @Test
    public void clearCompanyMoney() {
        // 清空账户
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("admin_name", "admin");
        admin = adminMapper.selectOne(wrapper);
        admin.setCompanyMoney(0);
        adminMapper.updateById(admin);
    }

    // 数据恢复
    @Test
    public void recovery() {
        clearUser();
        clearOrder();
        clearCompanyMoney();
        productRecovery();
        insertUser();
    }
    // 检验秒杀结果
    @Test
    public void check() {
        /*
        * 1 是否超卖
        * 2 检验公司账户入账是否和等于 售出产品数量*秒杀价
        * 3 检验订单表是否重复
        * 4 检验success字段用户购买是否成功，成功是否有订单表
        *  * */
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
        log.info("一共秒杀产品{}份，秒杀结束后产品数量{}",10000-leftAmount,leftAmount);
        log.info("销售金额:{}，公司入账:{}",sales,money);
        log.info("order表中订单数:{}",orderNum);
        log.info("user表中成功购买的人数:{}",successNum);
        log.info("user表中用户数:{}",allNum);

    }

}
