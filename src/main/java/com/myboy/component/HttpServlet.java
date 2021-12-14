package com.myboy.component;

import com.myboy.http.HttpServletRequest;
import com.myboy.http.HttpServletResponse;
import com.myboy.http.emuns.HttpMethod;

import java.io.IOException;

public abstract class HttpServlet {

    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getMethod().equals(HttpMethod.GET)){
            doGet(request, response);
        }else {
            doPost(request, response);
        }
    }

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException;
    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
