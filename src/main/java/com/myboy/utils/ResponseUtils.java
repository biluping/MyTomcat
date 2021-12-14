package com.myboy.utils;

import com.myboy.http.HttpServletResponse;
import com.myboy.http.emuns.ContentType;
import com.myboy.http.emuns.ResponseCode;

import java.io.IOException;

public class ResponseUtils {

    public static void write(HttpServletResponse response, ResponseCode responseCode, String msg) throws IOException {
        response.setCode(responseCode);
        response.setContentType(ContentType.JSON);
        response.write(msg);
    }
}
