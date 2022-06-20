package com.ff.sxbank.exception;

/**
 * 功能描述:
 * @Param:
 * @return:
 * @Author: 徐立峰
 * 时间: 2/28/2022 4:49 PM
 */
public enum ExceptionEnum implements CustomizedExceptionInterface {

    SUCCESS("200", "成功!"),
    ERROR("500", "失败!"),
    BODY_NOT_MATCH("4000","请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("4001","请求的数字签名不匹配!"),
    NOT_FOUND("4004", "未找到该资源!"),
    INTERNAL_SERVER_ERROR("5000", "服务器内部错误!"),
    SERVER_BUSY("5003","服务器正忙，请稍后再试!");

    private final String resultCode;

    private final String resultMsg;

    ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }


    @Override
    public String getResultCode() {
        return this.resultCode;
    }

    @Override
    public String getResultMsg() {
        return this.resultMsg;
    }
}
