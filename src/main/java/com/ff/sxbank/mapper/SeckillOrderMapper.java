package com.ff.sxbank.mapper;

import com.ff.sxbank.pojo.SeckillOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-20
 */
@Component
public interface SeckillOrderMapper extends BaseMapper<SeckillOrder> {

    @Update("truncate table seckill_order")
    void delete();

    @Select("SELECT user.username,seckill_order.user_identity FROM user,seckill_order WHERE user.identity_number=seckill_order.user_identity and user.success=1")
    List<Object> check1();

    @Select("select count(seckill_order.user_identity) from seckill_order where seckill_order.username like '压测%'")
    int selectOrderNum();

    @Select("select seckill_order.username,seckill_order.user_identity from seckill_order where seckill_order.user_identity=seckill_order.user_identity")
    List<Object> selectRepeat();


}
