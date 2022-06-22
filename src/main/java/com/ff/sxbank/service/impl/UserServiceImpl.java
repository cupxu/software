package com.ff.sxbank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.ApplyRecord;
import com.ff.sxbank.pojo.User;
import com.ff.sxbank.mapper.UserMapper;
import com.ff.sxbank.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.sxbank.sm4.SM4Utils;
import com.ff.sxbank.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ff
 * @since 2022-02-28
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    OverdueRecordServiceImpl overdueRecordService;
    @Autowired
    public void setOverdueRecordService(OverdueRecordServiceImpl overdueRecordService) {
        this.overdueRecordService = overdueRecordService;
    }

    ApplyRecordServiceImpl applyRecordService;
    @Autowired
    public void setApplyRecordService(ApplyRecordServiceImpl applyRecordService) {
        this.applyRecordService = applyRecordService;
    }

    SeckillProductServiceImpl seckillProductService;
    @Autowired
    public void setSeckillProductService(SeckillProductServiceImpl seckillProductService) {
        this.seckillProductService = seckillProductService;
    }

    @Override
    public int userRegister(User user) {
        int result = 0;
        try {
            result = userMapper.insert(user);
            return result;
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return result;
        }
    }

    @Override
    public ResponseResult userLogin(User user) {
        Map<String, String> result = new HashMap<>();
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("username", user.getUsername());
            String pass = user.getPassword();
            wrapper.eq("password", pass);
            User userDB = userMapper.selectOne(wrapper);
            log.info("user from db :{}",userDB);
            if (userDB == null) {
                log.info("user from db :{}","查无此人 请先注册");
                return ResponseResult.error("没有找到这个人");
            } else {
                //生成token
                try {
                    //payload 包含用户姓名 身份证号
                    Map<String, String> map = new HashMap<>();
                    map.put("username", userDB.getUsername());
                    map.put("identityNumber",userDB.getIdentityNumber());
                    String token = JWTUtils.createToken(map);
                    result.put("token", token);
                    log.info("初次登录成功,生成token:{}",token);
                } catch (Exception e) {
                    return ResponseResult.error(e.getMessage());
                }
                // 筛选用户信息 写用户申请记录记录 默认所有用户都是可以秒杀的
                try {
                    ResponseResult result1 = overdueRecordService.userSift(userDB.getIdentityNumber());
                    String isSuccess = "0";
                    if (result1.getCode().equals("200")) {
                        isSuccess = "1";
                        result.put("siftState", "200");
                    }else {
                        result.put("siftState", "500");
                    }
                    // 写登录状态
                    ResponseResult result2 = applyRecordService.writeRecord(userDB, isSuccess);

                    return ResponseResult.success(result);
                } catch (Exception e) {
                    log.info("筛选信息出现错误:{}",e.getMessage());
                    return ResponseResult.error("500509",e.getMessage(),result);
                }
            }
        } catch (Exception e) {
            log.info("登录出现故障{}",e.getMessage());
            return ResponseResult.error(e.getMessage());
        }
    }

    @Override
    public int userDelete(User user) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("username", user.getUsername());
            wrapper.eq("password", user.getPassword());
            wrapper.eq("identity_number", user.getIdentityNumber());
            int delete = userMapper.delete(wrapper);
            System.out.println(delete);
            return 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 1;
        }
    }

    @Override
    public ResponseResult userSuccess() {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("success", 1);
            List<User> list = userMapper.selectList(wrapper);
            return ResponseResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return ResponseResult.error("500", e.getMessage());
        }
    }
    @Override
    public ResponseResult selectByDate(String begin, String end) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            if (!begin.isEmpty()){
                wrapper.ge("last_login_time",begin);
            }
            if (!end.isEmpty()){
                wrapper.le("last_login_time",end);
            }
            List<ApplyRecord> list = userMapper.selectList(wrapper);
            return ResponseResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return ResponseResult.error("按时间范围查询出错啦");
        }
    }

    @Override
    public int check(String identityNumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("identity_number", identityNumber);
        if (userMapper.selectOne(wrapper) == null) {
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public ResponseResult selectAll() {
        return ResponseResult.success(userMapper.selectList(null));
    }

    @Override
    public ResponseResult deleteById(int id) {
        return ResponseResult.success(userMapper.deleteById(id));
    }

    // 获取年龄分组数据
    @Override
    public List<Object> getUserInterval() {
        List<Object> obj = userMapper.getUserInterval();
        log.info("分组数据:{}", obj);
        return obj;
    }

    @Override
    public ResponseResult selectByName(String name) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", name);
        try {
            List<ApplyRecord> list = userMapper.selectList(wrapper);
            return ResponseResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("按姓名查询出错");
        }
    }


}
