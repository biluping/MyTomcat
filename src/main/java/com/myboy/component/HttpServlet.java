package com.myboy.component;

import com.myboy.http.HttpServletRequest;
import com.myboy.http.HttpServletResponse;
import com.myboy.http.emuns.HttpMethod;

public abstract class HttpServlet {

    public void doService(HttpServletRequest request, HttpServletResponse response){
        if (request.getMethod().equals(HttpMethod.GET)){
            doGet(request, response);
        }else {
            doPost(request, response);
        }
    }

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response);
    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response);

}
