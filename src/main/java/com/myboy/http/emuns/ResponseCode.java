package com.myboy.http.emuns;

public enum ResponseCode {
    OK(200, "OK"),
    BAD_REQUEST(400,"Bad Request"),
    NOT_FOUND(404, "Not Found");

    private final Integer code;
    private final String msg;

    ResponseCode(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
