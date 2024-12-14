package com.yibu.yibuJudge.model.response;

import com.yibu.yibuJudge.constant.UserConstant;

public class Result<T> {

    private int code;
    private String message;
    private T data;

    public Result() {
    }
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }



    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    public static<T> Result<T> error() {
        return new Result<>(500, "error", null);
    }

    public static <T> Result<T> refresh() {
        return new Result<>(304, UserConstant.TOKEN_ERROR, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
