package com.ff.sxbank.service;

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.ApplyRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.sxbank.pojo.User;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-01
 */
public interface IApplyRecordService extends IService<ApplyRecord> {
    ResponseResult writeRecord(User user, String isSuccess);

    ResponseResult selectByDate(String begin, String end);

    ResponseResult selectByName(String name);

    ResponseResult selectAll();
}
