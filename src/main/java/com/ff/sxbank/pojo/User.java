package com.ff.sxbank.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 
 * </p>
 *
 * @author xulifeng
 * @since 2022-02-28
 */
@Component
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表主键
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 用户身份证号
     */
    private String identityNumber;

    /**
     * 注册时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date registerTime;

    /**
     * 用户最后登录时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastLoginTime;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 逻辑删除
     * 已删除为1 未删除为0
     */
    @TableLogic
    private Integer deleted;

    private Integer money;

    private Integer product;

    private String accountNumber;

    private Integer success;

    private Integer age;

    private String productName;


}
