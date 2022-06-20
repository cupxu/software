package com.ff.sxbank.exception;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 功能描述: 自定义异常接口
 * @Param:
 * @return:
 * @Author: 徐立峰
 * 时间: 2/28/2022 4:46 PM
 */
public interface CustomizedExceptionInterface {

    /**
     * @Description: 状态码
     * @Param:
     * @return:
     * @Author: xulifeng
     * @Date: 2/28/2022
     */
    String getResultCode();

    /**
     * @Description: 状态信息
     * @Param:
     * @return:
     * @Author: xulifeng
     * @Date: 2/28/2022
     */
    String getResultMsg();

}
