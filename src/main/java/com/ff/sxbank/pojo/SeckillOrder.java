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
 * @since 2022-03-20
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@TableName("seckill_order")
public class SeckillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 用户身份证号
     */
    private String userIdentity;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 产品id
     */
    private Integer productId;

    /**
     * 产品名
     */
    private String productName;

    /**
     * 购买数量
     */
    private Integer purchaseAmount;

    /**
     * 订单金额
     */
    private Double orderPrice;

    /**
     * 订单时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date orderTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;


}
