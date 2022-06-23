package com.ff.sxbank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.Admin;
import com.ff.sxbank.mapper.AdminMapper;
import com.ff.sxbank.pojo.User;
import com.ff.sxbank.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.sxbank.util.JWTUtils;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-04
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    AdminMapper adminMapper;
    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public ResponseResult adminRegister(Admin admin) {
        try {
            adminMapper.insert(admin);
            return ResponseResult.success("注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return ResponseResult.error(e.getMessage());
        }
    }

    @Override
    public ResponseResult adminLogin(Admin admin) {
        Map<String, String> result = new HashMap<>();
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("admin_name", admin.getAdminName());
            wrapper.eq("password", admin.getPassword());
            Admin adminDB = adminMapper.selectOne(wrapper);
            log.info("admin from db :{}", adminDB);
            if (adminDB == null) {
                log.info("admin from db :{}", "查无此人 请先注册");
                return ResponseResult.error("没有找到这个人");
            } else {
                //生成token
                try {
                    //payload 包含用户姓名 身份证号
                    Map<String, String> map = new HashMap<>();
                    map.put("adminName", adminDB.getAdminName());
                    map.put("identityNumber", adminDB.getIdentityNumber());
                    String token = JWTUtils.createToken(map);
                    result.put("token", token);
                    log.info("初次登录成功,生成token:{}", token);
                    return ResponseResult.success(result);
                } catch (Exception e) {
                    return ResponseResult.error(e.getMessage());
                }
                // 筛选用户信息 写用户申请记录记录 默认所有用户都是可以秒杀的
            }
        } catch (Exception e) {
            log.info("登录出现故障{}", e.getMessage());
            return ResponseResult.error(e.getMessage());
        }
    }

    @Override
    public int check(String username) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("admin_name", username);
        if (adminMapper.selectOne(wrapper) == null) {
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public ResponseResult getInfo() {
        QueryWrapper wrapper3 = new QueryWrapper();
        wrapper3.eq("admin_name", "admin");
        return ResponseResult.success(adminMapper.selectOne(wrapper3));
    }


    public Admin getAdmin() {
        QueryWrapper wrapper3 = new QueryWrapper();
        wrapper3.eq("admin_name", "admin");
        return adminMapper.selectOne(wrapper3);
    }




}
