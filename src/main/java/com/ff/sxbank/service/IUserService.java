package com.ff.sxbank.service;

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ff
 * @since 2022-02-28
 */
public interface IUserService extends IService<User> {
    int userRegister(User user);

    ResponseResult userLogin(User user);

    int userDelete(User user);

    ResponseResult userSuccess();

    ResponseResult selectByName(String name);

    ResponseResult selectByDate(String begin, String end);

    int check(String username);

    ResponseResult selectAll();


    ResponseResult deleteById(int id);

    Object getUserInterval();
}
