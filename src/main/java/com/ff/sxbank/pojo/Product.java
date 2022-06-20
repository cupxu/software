package com.ff.sxbank.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-04-01
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品id
     */
    @TableId(value = "product_id")
    private Integer productId;

    /**
     * 产品名
     */
    private String seckillProductName;

    /**
     * 产品数量
     */
    private Integer productScale;


    private String seckillTime;

    private String seckillEnd;

    private Double productRate;

    private String productPeriod;

    private String productReleaseStartDay;

    private String productReleaseEndDay;

    private String productHarvestDay;

    private String productExpireDay;

    private String productCode;

    private String productAddition;


}
