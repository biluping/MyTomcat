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

    public void write() throws IOException {
        write(null);
    }

    public void write(String data) throws IOException {
        String builder = String.format("HTTP/1.1 %s OK\n", code.getCode()) +
                String.format("Content-Type: %s;\n", contentType.getType()) +
                "\r\n" +
                (data == null ? code.getMsg() : data);
        outputStream.write(builder.getBytes(StandardCharsets.UTF_8));
    }
}
