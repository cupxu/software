package com.ff.sxbank.service;

import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.OverdueRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.sxbank.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-04
 */
public interface IOverdueRecordService extends IService<OverdueRecord> {
    ResponseResult userSift(String user);

    ResponseResult getAll();

    ResponseResult getOne(String target);

    ResponseResult add(Map<String,String> map);

    List<Map<String,Object>> getJoin();
}
