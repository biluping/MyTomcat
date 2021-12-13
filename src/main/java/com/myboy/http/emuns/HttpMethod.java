package com.myboy.http.emuns;

public enum HttpMethod {
    GET,POST;

    public static HttpMethod match(String method){
        switch (method){
            case "GET":
                return HttpMethod.GET;
            case "POST":
                return HttpMethod.POST;
            default:
                throw new RuntimeException("非法的http请求方法");
        }
    }
}
