package com.ff.sxbank.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 功能描述: 用户插入修改时间自动操作
 * @Param: metaObject
 * @return:
 * @Author: 徐立峰
 * 时间: 2/4/2022 10:03 AM
 */
@Component
public class MPTimeConfig implements MetaObjectHandler{

        /**
         * 使用mp做添加操作时候，这个方法执行
         * @param metaObject
         */
        @Override
        public void insertFill(MetaObject metaObject) {
            //设置属性值
            this.setFieldValByName("registerTime",new Date(),metaObject);
            this.setFieldValByName("lastLoginTime",new Date(),metaObject);
            this.setFieldValByName("applyDate",new Date(),metaObject);
            this.setFieldValByName("orderTime",new Date(),metaObject);

        }

        /**
         * 使用mp做修改操作时候，这个方法执行
         * @param metaObject
         */
        @Override
        public void updateFill(MetaObject metaObject) {
            this.setFieldValByName("lastLoginTime",new Date(),metaObject);
        }
}

