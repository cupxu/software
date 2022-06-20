package com.ff.sxbank.mapper;

import com.ff.sxbank.pojo.ApplyRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-01
 */
@Component
public interface ApplyRecordMapper extends BaseMapper<ApplyRecord> {

    @Select("SELECT distinct user_id,usernamem,applydate,is_success from apply_record")
    List<Map<String,Object>> getDistinct();
}
