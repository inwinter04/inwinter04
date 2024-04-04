package cn.iamdt.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MD5Utils {
    // 字符串秘钥，用于拼接到参数字符串的末尾（生成签到、发帖、评论的Sign时用到）
    private static final String KEY_SECRET = "fa1c28a5b62e79c3e63d9030b6142e4b";

    // 密钥字符串，用于拼接到参数字符串的末尾（生成图片上传的Sign时用到）
    private static final String SECRET_KEY = "my_sign@huluxia.com";


    // 生成发帖功能的sign
    public static String generateSign(String _key, String detail, String images, String title) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("_key", _key);
        params.put("detail", detail);
        params.put("device_code", "");
        params.put("images", images);
        params.put("title", title);
        params.put("voice", "");
        return calculateMD5(params);
    }

    // 生成回复评论功能的sign
    public static String generateSign(String _key, String post_id, String text) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("_key", _key);
        params.put("comment_id", "0");
        params.put("device_code", "");
        params.put("images", "");
        params.put("post_id", post_id);
        params.put("text", text);
        return calculateMD5(params);
    }

    // 生成签到功能的sign
    public static String generateSign(String cat_id, String time) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("cat_id", cat_id);
        params.put("time", time);
        return calculateMD5(params);
    }

    // 生成图片上传功能的sign
    public static String generatePicSign(String _key, String nonce_str, long timeMillis) {
        // 生成nonce_str，此代码即将移动至生成请求体时调用
        // String nonce_str = generateNonceStrUsingSecureRandom();

        // 拼接参数字符串
        String paramsStr = "_key=" + _key
                + "&app_version=4.3.0.2"
                + "&device_code=[d]1ed2762d-4019-4e37-8658-442c9467ec63"
                + "&gkey=000000"
                + "&market_id=floor_web"
                + "&nonce_str=" + nonce_str
                + "&platform=2"
                + "&timestamp=" + timeMillis
                + "&use_type=2"
                + "&versioncode=20141492"
                + "&secret=" + SECRET_KEY;

        // 计算MD5并返回
        return Objects.requireNonNull(getMD5String(paramsStr)).toUpperCase();
    }

    // 辅助方法：生成nonce_str (随机的32位字符串)
    public static String generateNonceStrUsingSecureRandom() {
        SecureRandom random = new SecureRandom();
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder nonceStr = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            nonceStr.append(chars[random.nextInt(chars.length)]);
        }
        return nonceStr.toString();
    }

    // 辅助方法：将参数排序并拼接，然后计算MD5
    private static String calculateMD5(TreeMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append(entry.getValue());
        }
        sb.append(KEY_SECRET);
        return getMD5String(sb.toString());
    }

    // 辅助方法：计算字符串的MD5
    private static String getMD5String(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = text.getBytes();
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            return byteArrayToHex(resultByteArray).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    // 辅助方法：将字节数组转换为十六进制字符串
    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }
}