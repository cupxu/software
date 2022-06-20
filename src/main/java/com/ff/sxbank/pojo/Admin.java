package com.ff.sxbank.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-03-04
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String password;
    /**
     * 管理员名字
     */
    private String adminName;

    /**
     * 电话
     */
    private String phoneNumber;

    /**
     * 身份证
     */
    private String identityNumber;

    /**
     * 工号
     */
    private String accountNumber;


    private Integer companyMoney;



}
