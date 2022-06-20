package com.ff.sxbank.mapper;

import com.ff.sxbank.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xulifeng
 * @since 2022-02-28
 */
@Component
public interface UserMapper extends BaseMapper<User> {

    @Delete("delete from user where user.username like '压测%'")
    int clear();

    @Select("select count(user.username) from user where user.success=1")
    int selectSuccessNum();

    @Select("select count(user.username) from user")
    int selectAll();

}
