package com.ff.sxbank.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-01
 */
@Data
@Component
@TableName("apply_record")
@EqualsAndHashCode(callSuper = false)
public class ApplyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户秒杀记录id
     */
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Integer applyId;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户身份证号
     */
    private String identityNumber;

    /**
     * 用户申请时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date applyDate;

    /**
     * 申请状态 默认失败
     */
    private String isSuccess;



}
