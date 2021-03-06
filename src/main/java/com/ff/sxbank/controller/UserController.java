package com.ff.sxbank.controller;

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.mapper.OverdueRecordMapper;
import com.ff.sxbank.pojo.OverdueRecord;
import com.ff.sxbank.pojo.User;
import com.ff.sxbank.service.impl.SeckillProductServiceImpl;
import com.ff.sxbank.service.impl.UserServiceImpl;
import com.ff.sxbank.util.CalculateAge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ff
 * @since 2022-02-28
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    public User user;
    @Autowired
    public void setUser(User user) {
        this.user = user;
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

    OverdueRecord overdueRecord;
    @Autowired
    public void setOverdueRecord(OverdueRecord overdueRecord) {
        this.overdueRecord = overdueRecord;
    }

    OverdueRecordMapper overdueRecordMapper;
    @Autowired
    public void setOverdueRecordMapper(OverdueRecordMapper overdueRecordMapper) {
        this.overdueRecordMapper = overdueRecordMapper;
    }

    @GetMapping("/check/{identityNumber}")
    public String checkRepeat(@PathVariable("identityNumber") String identityNumber) {
        log.info("username:{}",identityNumber);
        return String.valueOf(userService.check(identityNumber));
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseResult register(@RequestBody Map<String,String> registerUser) {
        try {
            System.out.println(registerUser);
            user.setUsername(registerUser.get("username"));
            user.setPassword(registerUser.get("password"));
            user.setPhoneNumber(registerUser.get("phoneNumber"));
            user.setIdentityNumber(registerUser.get("identityNumber"));
            user.setAge(CalculateAge.idCardToAge(registerUser.get("identityNumber")));
            user.setAccountNumber(registerUser.get("cardNumber"));
            // 随机生成0 - 15000之间的金额
            user.setMoney(new Random().nextInt(15000));
            String result = String.valueOf(userService.userRegister(user));
            log.info("用户信息：{}",user);
            return ResponseResult.success(result,"注册结果");
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseResult.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseResult login(@RequestBody Map<String, String> loginUser) {

        try {
            //根据姓名 身份证 id登录 查询是否有这个人 有这个人则生成token发送给用户
            user.setUsername(loginUser.get("username"));
            user.setPassword(loginUser.get("password"));
            //user.setIdentityNumber(loginUser.get("identityNumber"));
            log.info("用户提交的信息:{}",loginUser);
            return userService.userLogin(user);
        } catch (Exception e) {
            log.info("登录出现故障");
            return ResponseResult.error("50001", "登录出现问题");
        }
    }

    @PostMapping("/delete")
    public ResponseResult delete(@RequestBody Map<String, String> deleteUser) {
        try {
            //根据姓名 身份证 id登录 登录后发送token
            user.setUsername(deleteUser.get("username"));
            user.setPassword(deleteUser.get("password"));
            user.setIdentityNumber(deleteUser.get("identityNumber"));
            userService.userDelete(user);
            return ResponseResult.success("成功删除");
        } catch (Exception e) {
            log.info(String.valueOf(e.getStackTrace()));
            return ResponseResult.error(String.valueOf(e.getStackTrace()));
        }
    }

    @PostMapping("/deleteById")
    public ResponseResult deleteById(@RequestBody Map<String,String> map) {
        int id = Integer.parseInt(map.get("id"));
        log.info("id:{}",id);
        return ResponseResult.success(userService.deleteById(id));
    }

    @GetMapping("/selectByName")
    public ResponseResult selectByName(@RequestParam String name) {
        return userService.selectByName(name);
    }

    @PostMapping("/selectByDate")
    public ResponseResult selectByDate(@RequestBody Map<String,String> map) {
        log.info(map.get("startTime"));
        log.info(map.get("endTime"));
        String begin = map.get("startTime")+" 00:00:00";
        String end = map.get("endTime")+" 00:00:00";

        return userService.selectByDate(begin, end);
    }

    // 查询所有成功参与秒杀的用户
    @GetMapping("/result")
    public ResponseResult gerResult() {
        return userService.userSuccess();
    }

    @GetMapping("/allUsers")
    public String get(Model model) {
        model.addAttribute("allUsers",userService.selectAll());
        return "admin_users";
    }

    @GetMapping("/test")
    @ResponseBody
    public ResponseResult test() {
        log.info("测试");
        return ResponseResult.success("测试成功");
    }

    @GetMapping("/admin-index")
    public Object getIntervalData(Model model){
        model.addAttribute("userAge", userService.getUserInterval());
        model.addAttribute("productList",seckillProductService.getAllSecKillProducts().getResult());
        return "admin_index_back";
    }
}
