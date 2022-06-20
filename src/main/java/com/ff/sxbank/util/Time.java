package com.ff.sxbank.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-03-10 20:37
 **/
@Data
public class Time {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "查询开始时间", example = "2019-01-01 10:10:10")
        private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换


        @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
        private String end;

    }


