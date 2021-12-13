package com.myboy.http;

import com.myboy.http.emuns.ContentType;
import com.myboy.http.emuns.ResponseCode;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Setter@ToString
public class HttpServletResponse {

    private ResponseCode code;
    private ContentType contentType = ContentType.TXT_HTML;
    private OutputStream outputStream;

    public HttpServletResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String data){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("HTTP/1.1 %s OK\n", code.getCode()))
                .append(String.format("Content-Type: %s;\n", contentType.getType()))
                .append("\r\n")
                .append(data);
        try {
            outputStream.write(builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("写入数据失败");
        }
    }
}
