package com.ff.sxbank.mapper;

import com.ff.sxbank.pojo.OverdueRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import javafx.scene.control.Pagination;
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
 * @since 2022-03-04
 */
@Component
public interface OverdueRecordMapper extends BaseMapper<OverdueRecord> {



    @Select("SELECT user.*,overdue_record.* FROM user,overdue_record WHERE user.identity_number=overdue_record.identity_number")
    List<Map<String,Object>> getJoin();
}
