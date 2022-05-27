package com.thxbrop.springboot;

public class Result<T> {
    private final T data;
    private final String message;

    public Result(T data) {
        this.data = data;
        this.message = "success";
    }

    public Result(String message) {
        this.message = message;
        this.data = null;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public boolean success() {
        return data != null;
    }
}
