package com.myboy;

import com.myboy.component.HttpServlet;
import com.myboy.http.HttpServletRequest;
import com.myboy.http.HttpServletResponse;
import com.myboy.http.emuns.ResponseCode;
import com.myboy.thread.TomcatThreadPool;
import com.myboy.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

public class Tomcat {

    Logger logger = LoggerFactory.getLogger(Tomcat.class);

    private static final Properties properties;
    private static final ThreadPoolExecutor executor;

    static {
        // 初始化配置文件
        InputStream inputStream = Tomcat.class.getClassLoader().getResourceAsStream("tomcat.properties");
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("无法加载到配置文件：tomcat.properties");
        }

        // 初始化线程池
        TomcatThreadPool.init(properties);
        executor = TomcatThreadPool.getExecutor();

    }

    public void start() {
        int port = Integer.parseInt(properties.getProperty("server.port", "80"));
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            for (; ; ) {
                if (logger.isInfoEnabled()){
                    logger.info("Tomcat 启动成功，端口号{}", port);
                }
                Socket socket = serverSocket.accept();
                execute(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void execute(Socket socket) {
        executor.execute(() -> {
            try {
                HttpServletRequest request = new HttpServletRequest(socket.getInputStream());
                HttpServletResponse response = new HttpServletResponse(socket.getOutputStream());

                String className = properties.getProperty(request.getPath());

                // 未定义的Servlet
                if (className == null) {
                    ResponseUtils.write(response, ResponseCode.Not_Found, "接口不存在");
                    socket.close();
                    return;
                }

                System.out.println(Thread.currentThread().getName() + " " + request.getPath());
                Class<?> servletClass = Class.forName(className);
                HttpServlet httpServlet = (HttpServlet) servletClass.newInstance();
                httpServlet.doService(request, response);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });
    }

}
