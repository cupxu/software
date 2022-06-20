package com.ff.sxbank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.pojo.OverdueRecord;
import com.ff.sxbank.mapper.OverdueRecordMapper;
import com.ff.sxbank.service.IOverdueRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.sxbank.util.CalculateAge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class OverdueRecordServiceImpl extends ServiceImpl<OverdueRecordMapper, OverdueRecord> implements IOverdueRecordService {
    OverdueRecordMapper mapper;
    @Autowired
    public void setMapper(OverdueRecordMapper mapper) {
        this.mapper = mapper;
    }

    OverdueRecord record;
    @Autowired
    public void setRecord(OverdueRecord record) {
        this.record = record;
    }

    /**
    * @Description:
    * @Param:
    * @return:
    * @Author: xulifeng
    * @Date: 3/4/2022
    */
    @Override
    public ResponseResult userSift(String identityNumber) {

        // 根据user信息，在overdue_record表格查询用户的逾期记录
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("identity_number", identityNumber);
        try {
            OverdueRecord userSiftDB = mapper.selectOne(wrapper);
            //如果没有这个人的记录
            if (userSiftDB == null) {
                return ResponseResult.success("200","没有用户信息，默认允许购买");
            }
            // 计算指标
            int score = 0;

            // 计算年龄指标
            int age = CalculateAge.idCardToAge(identityNumber);
            if (age < 18) {
                return ResponseResult.error("500","初筛成功，不允许购买");
            }
            if (age >= 18 && age <=29) {
                score += 15;
            } else if (age >= 30 && age <= 50) {
                score += 20;
            } else if (age > 50) {
                score += 10;
            }

            // 计算贷款指标
            switch(userSiftDB.getIsBorrow()){
                case 0 :
                    score += 10;
                    break;
                case 1 :
                    score += 5;
                    break;
            }

            // 计算存款指标
            switch(userSiftDB.getIsStore()){
                case 0 :
                    score += 5;
                    break;
                case 1 :
                    score += 10;
                    break;
            }

            // 计算逾期次数指标
            if (userSiftDB.getOverdueTimes() < 2) {
                score += 5;
            }

            // 计算逾期时间指标
            if (userSiftDB.getOverdueDays() < 90) {
                score += 10;
            }

            // 计算工作指标
            switch(userSiftDB.getWorkingState()){
                case 0 :
                    score += 5;
                    break;
                case 1 :
                    score += 10;
                    break;
                case 2 :
                    score += 15;
                    break;
            }

            // 返回校验状态
            if (score > 35) {
                return ResponseResult.success("200","初筛成功，允许购买");
            } else {
                return ResponseResult.error("500","初筛成功，不允许购买");
            }
        } catch (Exception e) {
            return ResponseResult.error("500",e.getMessage());
        }
    }

    @Override
    public ResponseResult getAll() {
        try {
            return ResponseResult.success(mapper.selectList(null));
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Override
    public ResponseResult getOne(String target) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("identity_number",target);
            return ResponseResult.success(mapper.selectList(wrapper));
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Override
    public ResponseResult add(Map<String, String> map) {
        try {
            record.setIsStore(Integer.parseInt(map.get("isStore")));
            record.setUsername(map.get("username"));
            record.setOverdueTimes(Integer.parseInt(map.get("overdueTimes")));
            record.setOverdueDays(Integer.parseInt(map.get("overdueDays")));
            record.setIdentityNumber(map.get("identityNumber"));
            record.setIsBorrow(map.get("isBorrow").equals("是") ? 0 : 1);
            record.setWorkingState(Integer.parseInt(map.get("workingState")));

            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("identity_number",map.get("identityNumber"));
            OverdueRecord overdueRecord = mapper.selectOne(wrapper);
            if (overdueRecord == null) {
                mapper.insert(record);
            } else {
                record.setUserId(overdueRecord.getUserId());
                mapper.updateById(record);
            }
            return ResponseResult.success("200","成功添加筛选信息");
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Override
    public List<Map<String,Object>> getJoin() {
        try {
            return mapper.getJoin();
        } catch (Exception e) {
            return null;
        }
    }
}
