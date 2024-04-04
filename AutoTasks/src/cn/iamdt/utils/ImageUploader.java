package cn.iamdt.utils;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ImageUploader {
    // 此方法用于上传图片并返回fid
    public static String uploadImage(String key, File imageFile) throws IOException {
        String boundary = UUID.randomUUID().toString();
        String requestURL = "http://upload.huluxia.com/upload/v3/image";

        // 获取当前时间戳
        long timeMillis = System.currentTimeMillis();
        // 生成nonce_str
        String nonce_str = MD5Utils.generateNonceStrUsingSecureRandom();
        // 获得上传图片所需要的sign
        String sign = MD5Utils.generatePicSign(key, nonce_str, timeMillis);

        // 请求参数
        String parameters = "platform=2&gkey=000000&app_version=4.3.0.2&versioncode=20141492&market_id=floor_web&_key=" + key + "&device_code=%5Bd%5D1ed2762d-4019-4e37-8658-442c9467ec63&use_type=2&sign=" + sign + "&timestamp=" + timeMillis + "&nonce_str=" + nonce_str;

        // 使用HttpConnection类的方法来获取连接
        HttpURLConnection httpConn = HttpConnection.getMultipartHttpURLConnection(requestURL + "?" + parameters, boundary);

        OutputStream outputStream = httpConn.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

        // 发送文件数据
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(imageFile.getName()).append("\"\r\n");
        writer.append("Content-Type: ").append(HttpURLConnection.guessContentTypeFromName(imageFile.getName())).append("\r\n");
        writer.append("\r\n");
        writer.flush();

        FileInputStream inputStream = new FileInputStream(imageFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append("\r\n").flush();
        writer.append("--").append(boundary).append("--").append("\r\n");
        writer.close();

        // 检查服务器的响应代码
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            String fid = getFid(httpConn);

            httpConn.disconnect();
            return fid; // 返回fid
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
    }

    private static String getFid(HttpURLConnection httpConn) throws IOException {
        InputStream responseStream = new BufferedInputStream(httpConn.getInputStream());
        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8));

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        responseStreamReader.close();

        // 解析返回的内容
        JSONObject response = new JSONObject(stringBuilder.toString());
        System.out.println(response);
        return response.getString("fid");
    }
}
