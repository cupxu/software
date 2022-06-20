package com.ff.sxbank.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.Admin;
import com.ff.sxbank.service.impl.AdminServiceImpl;
import com.ff.sxbank.sm4.SM4Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description: 管理员
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-03-16 17:32
 **/
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    Admin admin;
    @Autowired
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    AdminServiceImpl adminService;
    @Autowired
    public void setAdminService(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/check/{username}")
    public String checkRepeat(@RequestParam("username") String username) {
        return String.valueOf(adminService.check(username));
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody Map<String,String> registerAdmin) {
        try {
            log.info("用户提交信息为：{}",registerAdmin);
            admin.setAdminName(registerAdmin.get("adminName"));
            String encryptedPassword = SM4Utils.encryptData_CBC(registerAdmin.get("password"));
            admin.setPassword(encryptedPassword);
            admin.setPhoneNumber(registerAdmin.get("phoneNumber"));
            admin.setAccountNumber(registerAdmin.get("accountNumber"));
            admin.setIdentityNumber(registerAdmin.get("identityNumber"));
            admin.setCompanyMoney(0);
            return adminService.adminRegister(admin);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseResult.success(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody Map<String, String> loginAdmin) {

        try {
            //根据姓名 身份证 id登录 查询是否有这个人 有这个人则生成token发送给用户
            admin.setAdminName(loginAdmin.get("adminName"));
            String encryptedPassword = SM4Utils.encryptData_CBC(loginAdmin.get("password"));
            admin.setPassword(encryptedPassword);
            log.info("用户提交的信息:{}",loginAdmin);
            return adminService.adminLogin(admin);
        } catch (Exception e) {
            log.info("登录出现故障");
            return ResponseResult.error("50001", "登录出现问题");
        }
    }

    @GetMapping("/info")
    public ResponseResult info() {
        return adminService.getInfo();
    }

}
