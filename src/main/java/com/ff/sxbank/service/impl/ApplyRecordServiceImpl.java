package com.ff.sxbank.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.ApplyRecord;
import com.ff.sxbank.mapper.ApplyRecordMapper;
import com.ff.sxbank.pojo.User;
import com.ff.sxbank.service.IApplyRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-01
 */
@Service
@Slf4j
public class ApplyRecordServiceImpl extends ServiceImpl<ApplyRecordMapper, ApplyRecord> implements IApplyRecordService {

    ApplyRecordMapper applyRecordMapper;
    @Autowired
    public void setApplyRecordMapper(ApplyRecordMapper applyRecordMapper) {
        this.applyRecordMapper = applyRecordMapper;
    }

    ApplyRecord applyRecord;
    @Autowired
    public void setApplyRecord(ApplyRecord applyRecord) {
        this.applyRecord = applyRecord;
    }

    @Override
    public ResponseResult writeRecord(User user, String isSuccess) {
        applyRecord.setIdentityNumber(user.getIdentityNumber());
        applyRecord.setUsername(user.getUsername());
        applyRecord.setIsSuccess(isSuccess);
        try {
            applyRecordMapper.insert(applyRecord);
            return ResponseResult.success("初筛成功");
        } catch (Exception e) {
            log.info("写用户申请信息报错:{}",e.getMessage());
            return ResponseResult.error("初筛失败");
        }
    }

    @Override
    public ResponseResult selectByDate(String begin, String end) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            if (!begin.isEmpty()){
                wrapper.ge("apply_date",begin);
            }
            if (!end.isEmpty()){
                wrapper.le("apply_date",end);
            }
            List<ApplyRecord> list = applyRecordMapper.selectList(wrapper);
            return ResponseResult.success("200","成功查询信息",list);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return ResponseResult.error("按时间范围查询出错啦");
        }
    }

    @Override
    public ResponseResult selectByName(String name) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", name);
        try {
            List<ApplyRecord> list = applyRecordMapper.selectList(wrapper);
            return ResponseResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("按姓名查询出错");
        }
    }

    @Override
    public ResponseResult selectAll() {
        try {
            return ResponseResult.success(applyRecordMapper.selectList(null));
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

}
