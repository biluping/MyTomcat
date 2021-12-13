package com.myboy;

import com.myboy.component.HttpServlet;
import com.myboy.http.HttpServletRequest;
import com.myboy.http.HttpServletResponse;
import com.myboy.http.emuns.ResponseCode;
import com.myboy.utils.ExceptionUtils;
import com.myboy.utils.ResponseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Tomcat {

    private Properties properties;

    public void start() {
        loadProperty();
        ServerSocket serverSocket = ExceptionUtils.newServerSocket(properties);
        for (; ; ) {
            try (Socket socket = serverSocket.accept()) {
                HttpServletRequest request = new HttpServletRequest(socket.getInputStream());
                HttpServletResponse response = new HttpServletResponse(socket.getOutputStream());

                String path = request.getPath();
                String className = properties.getProperty(path);

                // 未定义的Servlet
                if (className == null) {
                    ResponseUtils.write(response, ResponseCode.NOT_FOUND, "接口不存在");
                    socket.close();
                    continue;
                }

                Class<?> servletClass = ExceptionUtils.forName(className);
                HttpServlet httpServlet = (HttpServlet) ExceptionUtils.newInstance(servletClass);

                httpServlet.doService(request, response);

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("服务端出错了");
            }
        }
    }

    /**
     * 加载配置文件
     */
    private void loadProperty() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tomcat.properties");
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("无法加载到配置文件：tomcat.properties");
        }
    }

}
