package com.ff.sxbank.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: 响应结果
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-02-28 17:27
 **/
@Component
public class ResponseResult {

    private String code;
    private String msg;
    //可能要返回的查询信息
    private Object result;
    private String date;

    public ResponseResult() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ResponseResult(CustomizedExceptionInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.msg = errorInfo.getResultMsg();
    }

    //成功处理1
    public static ResponseResult success() {
        return success(null);
    }
    //成功 添加想要的信息
    public static ResponseResult success(Object data) {
        ResponseResult response = new ResponseResult();
        response.setCode(ExceptionEnum.SUCCESS.getResultCode());
        response.setMsg(ExceptionEnum.SUCCESS.getResultMsg());
        response.setResult(data);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        new Date();// 获取当前时间
        response.setDate(sdf.format(new Date()));
        return response;
    }

    //成功 添加想要的信息
    public static ResponseResult success(String msg) {
        ResponseResult response = new ResponseResult();
        response.setCode(ExceptionEnum.SUCCESS.getResultCode());
        response.setMsg(msg);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        new Date();// 获取当前时间
        response.setDate(sdf.format(new Date()));
        return response;
    }
    //成功 添加想要的信息
    public static ResponseResult success(String code, String msg) {
        ResponseResult response = new ResponseResult();
        response.setCode(ExceptionEnum.SUCCESS.getResultCode());
        response.setCode(code);
        response.setMsg(msg);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        new Date();// 获取当前时间
        response.setDate(sdf.format(new Date()));
        return response;
    }

    public static ResponseResult success(String code, String msg, Object obj) {
        ResponseResult response = new ResponseResult();
        response.setCode(ExceptionEnum.SUCCESS.getResultCode());
        response.setCode(code);
        response.setMsg(msg);
        response.setResult(obj);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        new Date();// 获取当前时间
        response.setDate(sdf.format(new Date()));
        return response;
    }

    //失败1
    public static ResponseResult error(CustomizedExceptionInterface errorInfo) {
        ResponseResult response = new ResponseResult();
        response.setCode(errorInfo.getResultCode());
        response.setMsg(errorInfo.getResultMsg());
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        new Date();// 获取当前时间
        response.setDate(sdf.format(new Date()));
        response.setResult(null);
        return response;
    }

    //失败2
    public static ResponseResult error(String code, String msg) {
        ResponseResult response = new ResponseResult();
        response.setCode(code);
        response.setMsg(msg);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        new Date();// 获取当前时间
        response.setDate(sdf.format(new Date()));
        response.setResult(null);
        return response;
    }

    //失败3
    public static ResponseResult error(String msg) {
        ResponseResult response = new ResponseResult();
        response.setCode("-1");
        response.setMsg(msg);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        new Date();// 获取当前时间
        response.setDate(sdf.format(new Date()));
        response.setResult(null);
        return response;
    }

    //失败4
    public static ResponseResult error(String code, String msg, Object o) {
        ResponseResult response = new ResponseResult();
        response.setCode(code);
        response.setMsg(msg);
        response.setResult(o);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        new Date();// 获取当前时间
        response.setDate(sdf.format(new Date()));
        response.setResult(null);
        return response;
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
