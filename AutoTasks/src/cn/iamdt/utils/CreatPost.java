package cn.iamdt.utils;

import cn.iamdt.utils.HttpConnection;
import cn.iamdt.utils.MD5Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CreatPost {
    /**
     * 创建帖子的静态方法
     *
     * @param userKey 用户登录秘钥
     * @param catId   板块ID代码
     * @param title   帖子标题
     * @param detail  帖子文本内容
     */
    public static void createPost(String userKey, int catId, String detail, String image, String title) {
        // 对标题和内容进行URL编码
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String encodedDetail = URLEncoder.encode(detail, StandardCharsets.UTF_8);

        // 生成数据验证Sign
        String sign = MD5Utils.generateSign(userKey, detail, image, title);

        // 构建请求的URL
        String urlString = "http://floor.huluxia.com/post/create/ANDROID/4.2.2?platform=2&gkey=000000&app_version=4.3.0.2&versioncode=20141492&market_id=floor_web&_key=" + userKey;
        // 构建请求参数，使用已编码的标题和内容
        String params = "draft_id=0&cat_id=" + catId + "&tag_id=-1&type=0&title=" + encodedTitle + "&detail=" + encodedDetail + "&patcha=&voice=&lng=0.0&lat=0.0&images=" + image + "&user_ids=&recommendTopics=&remindTopics=&sign=" + sign + "&is_app_link=3";

        try {
            HttpURLConnection con = HttpConnection.getHttpURLConnection(urlString, params);

            // 获取响应码
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 请求成功，读取响应内容
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    // 打印成功日志和响应内容
                    System.out.println("通讯成功，响应内容：" + response);
                }
            } else {
                // 请求失败，读取错误流内容
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    // 打印失败日志和错误响应内容
                    System.out.println("帖子创建失败，错误响应内容：" + errorResponse);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
