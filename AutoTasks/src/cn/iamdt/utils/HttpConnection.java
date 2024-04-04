package cn.iamdt.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpConnection {
    // 此方法用于普通的Http请求
    public static HttpURLConnection getHttpURLConnection(String urlString, String params) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        // 设置请求方法为POST
        con.setRequestMethod("POST");
        // 设置请求头
        con.setRequestProperty("Connection", "close");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", "305");
        con.setRequestProperty("Host", "floor.huluxia.com");
        con.setRequestProperty("Accept-Encoding", "gzip");
        con.setRequestProperty("User-Agent", "okhttp/3.8.1");
        // 允许输入输出流
        con.setDoOutput(true);

        // 发送POST请求参数
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = params.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return con;
    }

    // 此方法用于处理multipart/form-data类型的请求
    public static HttpURLConnection getMultipartHttpURLConnection(String requestURL, String boundary) throws IOException {
        URL url = new URL(requestURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        // 设置请求方法为POST
        con.setRequestMethod("POST");
        // 设置请求头
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.setRequestProperty("User-Agent", "okhttp/3.8.1");
        con.setRequestProperty("Accept-Encoding", "gzip");
        con.setRequestProperty("Connection", "close");
        con.setRequestProperty("Host", "upload.huluxia.com");
        return con;
    }
}
