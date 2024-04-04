package cn.iamdt.functions;

import java.util.Arrays;
import java.util.List;

public class EnvironmentVariablesReader {
    /**
     * 从环境变量读取userKey，并以字符串列表形式返回。
     *
     * @return 包含userKey的字符串列表。
     */
    public static List<String> getUserKeys() {
        // 获取名为USER_KEYS的环境变量的值
        String userKeys = System.getenv("USER_KEYS");
        // 如果环境变量的值不为空，则按照英文逗号分隔
        if (userKeys != null && !userKeys.isEmpty()) {
            // 分隔字符串并转换为列表
            return Arrays.asList(userKeys.split(","));
        }
        // 如果环境变量的值为空，则返回一个空列表
        return Arrays.asList();
    }

    // 主方法，用于测试
    public static void main(String[] args) {
        List<String> keys = getUserKeys();
        // 打印出读取到的userKey列表
        System.out.println("User Keys: " + keys);
    }
}
