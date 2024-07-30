package com.xiyue.common.vo;



import com.xiyue.common.enums.BusinessErrorCode;

import java.io.Serializable;

/**
 * 统一结果返回
 *
 * @param <T>
 */

@SuppressWarnings("unchecked")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 170853201891282512L;


    private int code;

    private String msg;

    private T data;

    private static final int SUCCESS_CODE = 0;
    private static final String SUCCESS_MSG = "success";

    private static final int ERROR_CODE = 500;
    private static final String ERROR_MSG = "error";

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
        this.code = SUCCESS_CODE;
    }


    public static <T> Result<T> ok() {
        return of(SUCCESS_CODE, SUCCESS_MSG, null);
    }


    public static <T> Result<T> ok(String mse) {
        return of(SUCCESS_CODE, mse, null);
    }

    public static <T> Result<T> ok(T data) {
        return of(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> Result<T> fail() {
        return of(ERROR_CODE, ERROR_MSG, null);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return of(code, msg, null);
    }

    public static <T> Result<T> fail(BusinessErrorCode code) {
        return of(code.getCode(), code.getErrMessage(), null);
    }

    public static <T> Result<T> fail(int code, String msg, T data) {
        return new Result(code, msg, data);
    }

    private static <T> Result<T> of(int code, String msg, T data) {
        return new Result(code, msg, data);
    }

    public static <T> Result<T> fail(BusinessErrorCode codeData, T data) {
        return new Result(codeData.getCode(), codeData.getErrMessage(), data);
    }


    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
