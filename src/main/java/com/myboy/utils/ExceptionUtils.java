package com.myboy.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

public class ExceptionUtils {

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("className %s 不存在", className));
        }
    }

    public static Object newInstance(Class<?> servletClass) {
        try {
            return servletClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("servlet 实例化失败");
        }
    }

    public static ServerSocket newServerSocket(Properties properties) {
        try {
            return new ServerSocket(Integer.parseInt(properties.getProperty("server.port", "80")));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("创建服务端Socket失败");
        }
    }
}
