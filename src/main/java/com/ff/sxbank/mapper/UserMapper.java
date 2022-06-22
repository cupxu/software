package com.ff.sxbank.mapper;

import com.ff.sxbank.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @Select("Select count(*),a.AgeGroup Count from (\n" +
            "select \n" +
            "Case \n" +
            "     when age between 18 and 30 then 'age1'\n" +
            "\t\t when age between 31 and 40 then 'age2'\n" +
            "\t\t when age between 41 and 50 then 'age3'\n" +
            "\t\t when age between 51 and 60 then 'age4'\n" +
            "     when  age > 60 then 'age5' end AgeGroup \n" +
            "from user ) a\n" +
            "group by a.AgeGroup\n" +
            "LIMIT 1,7")
    List<Object> getUserInterval();

}
