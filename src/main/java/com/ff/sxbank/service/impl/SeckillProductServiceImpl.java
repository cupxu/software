package com.ff.sxbank.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.sxbank.exception.CustomizedException;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.mapper.AdminMapper;
import com.ff.sxbank.mapper.SeckillOrderMapper;
import com.ff.sxbank.mapper.SeckillProductMapper;
import com.ff.sxbank.mapper.UserMapper;
import com.ff.sxbank.pojo.Admin;
import com.ff.sxbank.pojo.SeckillOrder;
import com.ff.sxbank.pojo.SeckillProduct;
import com.ff.sxbank.pojo.User;
import com.ff.sxbank.service.ISeckillProductService;
import com.ff.sxbank.util.DateUtil;
import com.ff.sxbank.util.JWTUtils;
import com.ff.sxbank.util.LinkUtil;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-01
 */
@Service
@Slf4j
public class SeckillProductServiceImpl extends ServiceImpl<SeckillProductMapper, SeckillProduct> implements ISeckillProductService {

    SeckillProductMapper seckillProductMapper;
    @Autowired
    public void setSeckillProductMapper(SeckillProductMapper seckillProductMapper) {
        this.seckillProductMapper = seckillProductMapper;
    }
    SeckillProduct seckillProduct;
    @Autowired
    public void setSeckillProduct(SeckillProduct seckillProduct) {
        this.seckillProduct = seckillProduct;
    }

    User user;
    @Autowired
    public void setUser(User user) {
        this.user = user;
    }

    UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    Admin admin;
    @Autowired
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    AdminMapper adminMapper;
    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    SeckillOrderMapper seckillOrderMapper;
    @Autowired
    public void setSeckillOrderMapper(SeckillOrderMapper seckillOrderMapper) {
        this.seckillOrderMapper = seckillOrderMapper;
    }

    SeckillOrder seckillOrder;
    @Autowired
    public void setSeckillOrder(SeckillOrder seckillOrder) {
        this.seckillOrder = seckillOrder;
    }


    @Override
    @Async("taskExecutor")
    @Transactional(rollbackFor=Exception.class) // 只要抛出异常就回滚  && 如果是内部方法添加@Transactional 还要同时暴露出AOP代理对象 确保事务原子性
    public CompletableFuture<ResponseResult> doSeckill(String identityNumber, String productId) {
        //毫秒ms：
        long startTime1=System.currentTimeMillis(); //获取开始时间

            try{

                //　查产品表 获取所有要用的字段 计算机的空间局部性
                seckillProduct = seckillProductMapper.selectById(productId);
                int amount = seckillProduct.getSeckillProductAmount();
                int productPrice = seckillProduct.getSeckillProductPrice();
                Date dateStart = seckillProduct.getSeckillTime();
                Date dateEnd = seckillProduct.getSeckillEnd();
                String productName = seckillProduct.getSeckillProductName();
                // 按优先级依次比较
                if (amount == 0) {
                    return CompletableFuture.completedFuture(ResponseResult.error("500101", "产品已售空"));
                }

                // 获取当前时间
                Date currentTime = DateUtil.strToDateLong(DateUtil.getStringDate());
                // 执行秒杀前查询用户订单表确认用户是否已经购买此产品
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("user_identity", identityNumber);
                wrapper.eq("product_id", productId);
                seckillOrder = seckillOrderMapper.selectOne(wrapper);
                if (seckillOrder != null) {
                    return CompletableFuture.completedFuture(ResponseResult.error("500105", "已购买秒杀产品，不能再次购买"));
                }

                // 获取管理员
                QueryWrapper wrapper3 = new QueryWrapper();
                wrapper3.eq("admin_name", "admin");
                admin = adminMapper.selectOne(wrapper3);
                int totalMoney = admin.getCompanyMoney();

                // 查用户表
                QueryWrapper wrapper1 = new QueryWrapper();
                wrapper1.eq("identity_number", identityNumber);
                user = userMapper.selectOne(wrapper1);
                int userMoney = user.getMoney();
                String userName = user.getUsername();

                if (userMoney < productPrice) {
                    return CompletableFuture.completedFuture(ResponseResult.error("500104", "您的账户金额不足"));
                }

                // 可移交其他功能 尽可能减少秒杀执行时间
                if (currentTime.before(dateStart)) {
                    return CompletableFuture.completedFuture(ResponseResult.error("500110", "秒杀尚未开始"));
                }
                if (currentTime.after(dateEnd)) {
                    return CompletableFuture.completedFuture(ResponseResult.error("500108", "秒杀已经结束"));
                }


                // 下面涉及具体的数据库更新操作，为确保事务成功 需要手动抛出异常

                // -------------------------- 减库存 付款入账 --原子性操作 乐观锁
                seckillProduct.setSeckillProductAmount(--amount);
                int seckillResult = seckillProductMapper.updateById(seckillProduct);
                if (seckillResult == 0) {
                    //直接返回错误信息 不会影响到其他数据库
                    return CompletableFuture.completedFuture(ResponseResult.error("500102", "没有抢到秒杀产品"));
                }

                // --------------------------用户扣款 生成订单
                user.setSuccess(1);
                user.setMoney(userMoney - productPrice);
                user.setProduct(Integer.parseInt(productId));
                user.setProductName(productName);
                int userResult = userMapper.updateById(user);
                if (userResult == 0) {
                    throw new CustomizedException("500109", "秒杀失败，用户信息修改出错");
                }

                // --------------------------公司入账
                admin.setCompanyMoney(totalMoney + productPrice);
                int adminResult = adminMapper.updateById(admin);
                if (adminResult == 0) {
                    throw new CustomizedException("500107", "秒杀失败，公司账户入账出错");
                }

                seckillOrder = new SeckillOrder();
                seckillOrder.setUserIdentity(identityNumber);
                seckillOrder.setUsername(userName);
                seckillOrder.setProductId(Integer.parseInt(productId));
                seckillOrder.setProductName(productName);
                seckillOrder.setPurchaseAmount(1);
                seckillOrder.setOrderPrice((double) productPrice);
                int orderFlag = seckillOrderMapper.insert(seckillOrder);
                if (orderFlag == 0) {
                    throw new CustomizedException("500108", "订单生成失败，产品无法购买");
                }

                long endTime1=System.currentTimeMillis(); //获取结束时间
                log.info("程序运行时间：{}ms ",(endTime1-startTime1));

                return CompletableFuture.completedFuture(ResponseResult.success("200010", "秒杀成功，用时"+(endTime1-startTime1)+"ms", seckillOrder));

            } catch (RejectedExecutionException e) {
                //释放锁
                e.printStackTrace();
                log.info("队列已满", e.getMessage());
                //@Transactional和try catch捕获异常会让注解失效
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return CompletableFuture.completedFuture(ResponseResult.error("500111", "当前访问人数过多，请稍后再试"));
            } catch (Exception e) {
                //释放锁
                e.printStackTrace();
                log.info("秒杀出现问题:{}", e.getMessage());
                //@Transactional和try catch捕获异常会让注解失效
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return CompletableFuture.completedFuture(ResponseResult.error("500103", e.getStackTrace().toString()));
            }
    }

    /** 
    * @Description: 获取所有秒杀产品
    * @Param: 
    * @return: 
    * @Author: xulifeng
    * @Date: 3/9/2022 
    */
    @Override
    public ResponseResult getAllSecKillProducts() {
        try {
            List<SeckillProduct> list = seckillProductMapper.selectList(null);
            return ResponseResult.success("200","成功获取所有秒杀产品",list);
        } catch (Exception e) {
            log.info("获取所有产品信息过程中出现问题，{}", e.getMessage());
            return ResponseResult.error(e.getMessage());
        }

    }

    /**
    * @Description: 更新秒杀产品
    * @Param: product
    * @return:
    * @Author: xulifeng
    * @Date: 3/9/2022
    */
    @Override
    public ResponseResult updateSecKillProduct(Map<String,String> product) {
        log.info("修改信息：{}",product);
        try {
            int id = Integer.parseInt(product.get("seckillProductId"));
            SeckillProduct seckillProduct = seckillProductMapper.selectById(id);
            seckillProduct.setSeckillProductName(product.get("seckillProductName"));
            seckillProduct.setSeckillProductPrice(Integer.parseInt(product.get("seckillProductPrice")));
            seckillProduct.setSeckillProductAmount(Integer.parseInt(product.get("seckillProductAmount")));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            seckillProduct.setSeckillTime(sdf.parse(product.get("seckillTime")));
            seckillProduct.setSeckillEnd(sdf.parse(product.get("seckillEnd")));
            log.info(product.get("seckillTime"));
            log.info(product.get("seckillEnd"));
            seckillProductMapper.updateById(seckillProduct);
            return ResponseResult.success( "秒杀产品更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("更新产品信息过程中出现问题，{}", e.getMessage());
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
    * @Description: 删除对应id商品
    * @Param: id
    * @return:
    * @Author: xulifeng
    * @Date: 3/9/2022
    */
    @Override
    public ResponseResult deleteSecKillProduct(String productId) {
        log.info(productId);
        try {
            seckillProductMapper.deleteById(Integer.parseInt(productId));
            return ResponseResult.success("秒杀产品成功删除");
        } catch (Exception e) {
            log.info("删除产品信息过程中出现问题，{}", e.getMessage());
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
    * @Description: 添加秒杀产品
    * @Param:
    * @return:
    * @Author: xulifeng
    * @Date: 3/9/2022
    */
    @Override
    public ResponseResult insertSecKillProduct(Map<String,String> product) {
        try {
            seckillProduct.setSeckillProductAmount(Integer.parseInt(product.get("seckillProductAmount")));
            seckillProduct.setSeckillProductName(product.get("seckillProductName"));
            seckillProduct.setSeckillProductPrice(Integer.parseInt(product.get("seckillProductPrice")));
            // 日期处理
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                seckillProduct.setSeckillTime(sdf.parse(product.get("seckillTime")));
                seckillProduct.setSeckillEnd(sdf.parse(product.get("seckillEnd")));
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseResult.error("时间出错");
            }
            // 后续增加字段

            seckillProductMapper.insert(seckillProduct);
            return ResponseResult.success("成功添加秒杀产品");
        } catch (Exception e) {
            log.info("添加产品出错:{}",e.getMessage());
            e.printStackTrace();
            return ResponseResult.error("500",e.getMessage());
        }

    }

    @Override
    public String generateLink(HttpServletRequest request, String productId) {
        String token = request.getHeader("Authorization");
        DecodedJWT userInfo = JWTUtils.getDecodedToken(token);
        //拿到用户的身份证号
        String identityNumber = userInfo.getClaim("identityNumber").asString();
        try {
            String link = LinkUtil.generateLink(productId + identityNumber);
            return link;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
