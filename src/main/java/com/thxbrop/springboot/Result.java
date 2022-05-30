package com.thxbrop.springboot;

public class Result<T> {
    private final T data;
    private final int code;
    private final String message;

    public Result(T data) {
        this.data = data;
        this.message = "success";
        this.code = 200;
    }

    public Result(String message, int code) {
        this.message = message;
        this.code = code;
        this.data = null;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public boolean success() {
        return data != null;
    }
}
