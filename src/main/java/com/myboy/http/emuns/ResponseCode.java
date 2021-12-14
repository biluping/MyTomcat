package com.myboy.http.emuns;

public enum ResponseCode {
    OK(200, "OK"),
    Bad_Request(400,"Bad Request"),
    Not_Found(404, "Not Found"),
    Internal_Server_Error(500, "Internal Server Error");

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
