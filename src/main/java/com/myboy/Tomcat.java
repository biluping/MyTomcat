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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class Tomcat {

    private static final Logger logger = LoggerFactory.getLogger(Tomcat.class);

    private static final Properties properties = new Properties();
    private static final ThreadPoolExecutor executor;
    private final Map<String, HttpServlet> handlerMapping = new ConcurrentHashMap<>();

    static {
        // 初始化配置文件
        InputStream inputStream = Tomcat.class.getClassLoader().getResourceAsStream("tomcat.properties");
        if (inputStream == null){
            if (logger.isDebugEnabled()){
                logger.debug("配置文件tomcat.properties不存在，将使用默认配置");
            }
        }

        try {
            if (inputStream != null){
                properties.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("加载到配置文件失败：tomcat.properties");
        }

        // 初始化线程池
        TomcatThreadPool.init(properties);
        executor = TomcatThreadPool.getExecutor();

    }

    public void start() {
        int port = Integer.parseInt(properties.getProperty("server.port", "80"));
        if (logger.isInfoEnabled()) {
            logger.info("Tomcat 启动成功，端口号{}", port);
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            for (; ; ) {
                Socket socket = serverSocket.accept();
                execute(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void execute(Socket socket) {
        executor.execute(() -> {
            long startTime = System.currentTimeMillis();
            String path = null;
            String className = null;

            try {
                HttpServletRequest request = new HttpServletRequest(socket.getInputStream());
                HttpServletResponse response = new HttpServletResponse(socket.getOutputStream());

                path = request.getPath();

                if (logger.isDebugEnabled()){
                    logger.debug("{} {}", Thread.currentThread().getName(), request.getPath());
                }

                HttpServlet httpServlet = handlerMapping.get(path);
                if (httpServlet == null){
                    className = properties.getProperty(path);
                    if (className == null){
                        ResponseUtils.write(response, ResponseCode.Not_Found, ResponseCode.Not_Found.getMsg());
                        return;
                    }
                    Class<?> servletClass = Class.forName(className);
                    httpServlet = (HttpServlet) servletClass.newInstance();
                    handlerMapping.put(path, httpServlet);
                }

                httpServlet.doService(request, response);


            } catch (ClassNotFoundException e){
                if (logger.isErrorEnabled()){
                    logger.error("类{}未定义", className);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    if (logger.isDebugEnabled()) {
                        logger.debug("接口{}执行时间：{}", path, System.currentTimeMillis() - startTime);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });
    }

}
