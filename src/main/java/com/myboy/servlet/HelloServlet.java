package com.myboy.servlet;

import com.myboy.component.HttpServlet;
import com.myboy.http.HttpServletRequest;
import com.myboy.http.HttpServletResponse;
import com.myboy.http.emuns.ContentType;
import com.myboy.http.emuns.ResponseCode;

public class HelloServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType(ContentType.JSON);
        response.setCode(ResponseCode.OK);
        response.write("doPost执行了");
    }
}
