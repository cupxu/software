package com.ff.sxbank.controller;

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.mapper.OverdueRecordMapper;
import com.ff.sxbank.pojo.OverdueRecord;
import com.ff.sxbank.pojo.User;
import com.ff.sxbank.service.impl.UserServiceImpl;
import com.ff.sxbank.sm4.SM4Utils;
import com.ff.sxbank.util.UserInfoUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
@RestController
@RequestMapping("/user")
public class UserController {

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
    public ResponseResult register(@RequestBody Map<String,String> registerUser) {
        try {
            System.out.println(registerUser);
            String username = registerUser.get("username");
            user.setUsername(username);
            String userRegisterPassword = registerUser.get("password");
            // 对用户密码加密后进行存储
            user.setPassword(userRegisterPassword);
            user.setPhoneNumber(registerUser.get("phoneNumber"));
            user.setIdentityNumber(registerUser.get("identityNumber"));
            user.setAccountNumber(registerUser.get("cardNumber"));
            user.setMoney(new Random().nextInt(15000));
            String result = String.valueOf(userService.userRegister(user));
            log.info("用户信息：{}",user);

            // 除了注册信息外还要添加用户对应的默认sift信息
            List<Integer> list = UserInfoUtil.generateInfo();
            log.info("overdueRecord信息：{}",list);
            overdueRecord.setIdentityNumber(registerUser.get("identityNumber"));
            overdueRecord.setUsername(registerUser.get("username"));
            overdueRecord.setIsBorrow(list.get(0));
            overdueRecord.setIsStore(list.get(1));
            overdueRecord.setWorkingState(list.get(2));
            overdueRecord.setOverdueDays(list.get(3));
            overdueRecord.setOverdueTimes(list.get(4));
            overdueRecordMapper.insert(overdueRecord);

            log.info(String.valueOf(overdueRecord));
            return ResponseResult.success(result,"注册成功");
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseResult.error(e.getMessage());
        }
    }

    @PostMapping("/login")
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
    public ResponseResult get() {
        return userService.selectAll();
    }

    @GetMapping("/test")
    public ResponseResult test() {
        log.info("测试");
        return ResponseResult.success("测试成功");
    }
}
