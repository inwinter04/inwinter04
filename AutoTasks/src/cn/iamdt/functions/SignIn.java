package cn.iamdt.functions;

import cn.iamdt.utils.HttpConnection;
import cn.iamdt.utils.MD5Utils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class SignIn {

    // API URL
    private static final String POST_URL = "http://floor.huluxia.com/user/signin/ANDROID/4.1.8";

    // 对所有分类板块执行签到
    public static void SignInForAllCategories(String USER_KEY) throws InterruptedException, IOException {
        int[] catIds = {1, 2, 3, 4, 6, 15, 16, 21, 22, 29, 43, 44, 45, 57, 58, 60, 63, 67, 68, 69, 70, 71, 76, 77, 81, 82, 84, 90, 92, 94, 96, 98, 102, 107, 108, 110, 111, 115, 119, 120, 121, 122};

        for (int catId : catIds) {
            long time = System.currentTimeMillis();
            String sign = MD5Utils.generateSign(Integer.toString(catId), String.valueOf(time));
            //noinspection SpellCheckingInspection
            String params = String.format("platform=2&gkey=000000&app_version=4.3.0.2&versioncode=20141492&market_id=floor_web&_key=%s&device_code=[d]1ed2762d-4019-4e37-8658-442c9467ec63&phone_brand_type=MI&hlx_imei=&hlx_android_id=effafaac8567c6d0&hlx_oaid=9d22479ec6e9f693&cat_id=%d&time=%d&sign=%s",
                    USER_KEY, catId, time, sign);

            HttpURLConnection connection = HttpConnection.getHttpURLConnection(POST_URL, params);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                printResponse(connection, catId);
            }

            Thread.sleep(1000);
        }
    }


    // 打印响应内容
    private static void printResponse(HttpURLConnection connection, int catId) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            int status = jsonResponse.getInt("status");
            if (status == 1) {
                int continueDays = jsonResponse.getInt("continueDays");
                int experienceVal = jsonResponse.getInt("experienceVal");
                System.out.println("板块ID为：" + catId +
                        " 签到状态：成功" +
                        " 获得经验：" + experienceVal +
                        " 连续签到天数：" + continueDays);

            } else {
                System.out.println("签到失败，请检查原因。");
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // 测试代码
        String userKey = "";
        // 进行签到
        SignInForAllCategories(userKey);
    }

}