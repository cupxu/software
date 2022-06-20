package com.ff.sxbank.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

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
 * @since 2022-03-01
 */
@Component
@Data
@EqualsAndHashCode(callSuper = false)
public class SeckillProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品id
     */
    @TableId(value = "seckill_product_id", type = IdType.AUTO)
    private Integer seckillProductId;

    /**
     * 产品名
     */
    private String seckillProductName;

    /**
     * 产品数量
     */
    private Integer seckillProductAmount;

    /**
     * 产品单价
     */
    private Integer seckillProductPrice;

    @TableLogic
    private Integer deleted;

    private Date seckillTime;

    private Date seckillEnd;


    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
