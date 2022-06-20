package com.ff.sxbank.service;

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-04
 */
public interface IAdminService extends IService<Admin> {

    ResponseResult adminRegister(Admin admin);

    ResponseResult adminLogin(Admin admin);

    int check(String username);

    ResponseResult getInfo();
}
