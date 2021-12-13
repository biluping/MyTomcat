package com.myboy.http;

import com.myboy.http.emuns.HttpMethod;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Data
public class HttpServletRequest {

    private String path;
    private HttpMethod method;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();
    private String body;

    public HttpServletRequest(InputStream inputStream){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // 请求行
            String line = reader.readLine();
            String[] requestLine = line.split("\\s");
            this.method = HttpMethod.match(requestLine[0]);

            String[] uri = requestLine[1].split("\\?");
            this.path = uri[0];

            // 参数
            if (uri.length == 2){
                String[] param = uri[1].split("&");
                for (String p : param) {
                    String[] keyValue = p.split("=");
                    if (keyValue.length == 2){
                        parameters.put(keyValue[0],keyValue[1]);
                    }
                }
            }

            // 请求头
            for(;;){
                line = reader.readLine();

                if ("".equals(line)){  // get请求结束符为\r\n，readLine() 方法读取后是 ""
                    return;
                }

                String[] head = line.split(": ");

                // 处理 POST 请求体
                if (line.startsWith("Content-Length")){
                    int len = Integer.parseInt(head[1]) + 2;  // Content-Length 请求头后面还有一个 /r/n，占两个字节

                    // 处理请求体
                    char[] data = new char[len];
                    int read = reader.read(data);
                    this.body = new String(data).replaceAll("\r\n","").replaceAll("\\s+","");
                    return;
                }

                headers.put(head[0], head[1]);

            }

        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("读取 InputStream 异常");
        }
    }

}
