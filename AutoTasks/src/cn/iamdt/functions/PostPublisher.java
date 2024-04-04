package cn.iamdt.functions;

import cn.iamdt.utils.ImageDownloader;
import cn.iamdt.utils.ImageUploader;
import cn.iamdt.utils.CreatPost;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PostPublisher {

    private static final String USER_KEY = "";
    private static final int CAT_ID = 92;
    private static final String DIRECTORY_PATH = "FileCollectionStream";

    public static void main(String[] args) {
        publishPost(USER_KEY);
    }

    public static void publishPost(String USER_KEY) {
        File dir = new File(DIRECTORY_PATH);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null && directoryListing.length > 0) {
            File selectedFile = getRandomFile(directoryListing);

            try {
                Map<String, Object> postData = parseJsonFile(selectedFile);
                String title = buildTitle(postData);
                String detail = buildDetail(postData);
                String imageParams = uploadImages(postData);

                System.out.println(USER_KEY + CAT_ID + detail + imageParams + title);
                CreatPost.createPost(USER_KEY, CAT_ID, detail, imageParams, title);
                deleteFile(selectedFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No files found in directory: " + DIRECTORY_PATH);
        }
    }

    private static File getRandomFile(File[] files) {
        Random rand = new Random();
        return files[rand.nextInt(files.length)];
    }

    private static Map<String, Object> parseJsonFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Map.class);
    }

    private static String buildTitle(Map<String, Object> data) {
        return "【模型资讯】" + data.get("title").toString();
    }

    private static String buildDetail(Map<String, Object> data) {
        StringBuilder detailBuilder = new StringBuilder();
        detailBuilder.append("<text>");
        data.forEach((key, value) -> {
            if (!"images".equals(key) && !"title".equals(key)) {
                detailBuilder.append(key).append(": ").append(value).append("\n");
            }
        });
        detailBuilder.append("</text>");
        return detailBuilder.toString();
    }

    private static String uploadImages(Map<String, Object> data) throws IOException {
        StringBuilder imageParams = new StringBuilder();
        List<String> images = (List<String>) data.get("images");
        int imageCount = 0;
        for (String imageUrl : images) {
            if (imageCount >= 9) {
                break;
            }
            File imageFile = ImageDownloader.downloadImage(imageUrl);
            String fid = ImageUploader.uploadImage(USER_KEY, imageFile);
            imageParams.append(fid).append(",");
            imageCount++;
        }
        return imageParams.toString();
    }

    private static void deleteFile(File file) throws IOException {
        Files.deleteIfExists(Paths.get(file.getAbsolutePath()));
        System.out.println("Posted and deleted file: " + file.getName());
    }
}