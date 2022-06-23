package com.ff.sxbank.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.Admin;
import com.ff.sxbank.service.impl.AdminServiceImpl;
import com.ff.sxbank.service.impl.SeckillProductServiceImpl;
import com.ff.sxbank.service.impl.UserServiceImpl;
import com.ff.sxbank.sm4.SM4Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@Controller
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

    public SeckillProductServiceImpl seckillProductService;
    @Autowired
    public void setSeckillProductService(SeckillProductServiceImpl seckillProductService) {
        this.seckillProductService = seckillProductService;
    }

    UserServiceImpl userService;
    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
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

    /**
     * 用户登录后主页
     * @param model
     * @return
     */
    @GetMapping("/index")
    public Object getIntervalData(Model model){
        // 用户年龄分组数据
        model.addAttribute("userAge", userService.getUserInterval());
        log.info("userAge,{}",userService.getUserInterval());
        // 公司账户金额
        model.addAttribute("companyMoney", adminService.getAdmin().getCompanyMoney());
        log.info("companyMoney,{}",adminService.getAdmin().getCompanyMoney());

        // 产品列表
        model.addAttribute("productList",seckillProductService.getAllSecKillProducts().getResult());
        log.info("productList,{}",seckillProductService.getAllSecKillProducts().getResult());

        // 用户列表
        model.addAttribute("userList",userService.selectAll().getResult());
        log.info("userList,{}",userService.selectAll().getResult());

        return "admin_index_back";
    }

    /**
     * 管理员查看所有用户
     * @param model
     * @return
     */
    @GetMapping("/all_users")
    public String get(Model model) {
        model.addAttribute("allUsers",userService.selectAll());
        return "admin_users";
    }



}
