package com.ff.sxbank.exception;

/**
 * @description: 自定义异常类
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-02-28 17:18
 **/

public class CustomizedException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    protected String errorCode;

    protected String errorMsg;

    public CustomizedException() {
        super();
    }

    public CustomizedException(CustomizedExceptionInterface exceptionInterface) {
        super(exceptionInterface.getResultCode());
        this.errorCode = exceptionInterface.getResultCode();
        this.errorMsg = exceptionInterface.getResultMsg();
    }
    public CustomizedException(CustomizedExceptionInterface exceptionInterface, Throwable cause) {
        super(exceptionInterface.getResultCode(),cause);
        this.errorCode = exceptionInterface.getResultCode();
        this.errorMsg = exceptionInterface.getResultMsg();
    }

    public CustomizedException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public CustomizedException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public CustomizedException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode,cause);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
