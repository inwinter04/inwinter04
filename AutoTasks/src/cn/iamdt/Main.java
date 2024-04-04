package cn.iamdt;

import cn.iamdt.functions.EnvironmentVariablesReader;
import cn.iamdt.functions.PostPublisher;
import cn.iamdt.functions.SignIn;

import java.io.IOException;
import java.util.List;

public class Main {
    /*
        自动任务程序入口
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        List<String> userKeys = EnvironmentVariablesReader.getUserKeys();

        if (!userKeys.isEmpty()) {
            for (int i = 0; i < userKeys.size(); i++) {
                System.out.println("开始执行第" + i + "个账号签到");
                SignIn.SignInForAllCategories(userKeys.get(i));
                System.out.println("开始执行第" + i + "个账号任务");
                PostPublisher.publishPost(userKeys.get(i));
            }
        } else {
            System.out.println("未读取到任何账号");
        }
        System.out.println("自动任务执行完毕！");
    }
}
