# MyTomcat
基于Java Bio 编写的简易版 Tomcat

# 使用方法
1. 新建一个 Servlet，继承自 HttpServlet，实现 doGet()、doPost() 方法
2. 在 ClassPath 下创建配置文件 tomcat.properties
3. 在配置文件中添加配置，配置文件中内容如下

```properties
# 服务器端口号
server.port=8080

# servelt 路径映射，如下所示：
/hello=com.myboy.servlet.HelloServlet
```
