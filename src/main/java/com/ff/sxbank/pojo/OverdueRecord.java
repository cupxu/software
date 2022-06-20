package com.ff.sxbank.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@TableName("overdue_record")
public class OverdueRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户秒杀记录id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户身份证号
     */
    private String identityNumber;

    /**
     * 用户近3年逾期次数
     */
    private Integer overdueTimes;

    /**
     * 逾期金额
     */
    private Integer isStore;

    /**
     * 逾期天数
     */
    private Integer overdueDays;

    /**
     * 就业状态 无业/失业
     */
    private Integer workingState;

    /**
     * 信用状态 失信
     */
    private Integer isBorrow;


}
