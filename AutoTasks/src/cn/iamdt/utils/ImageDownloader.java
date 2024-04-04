package cn.iamdt.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ImageDownloader {
    public static File downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        Path tempDir = Files.createTempDirectory("images");
        String fileName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
        Path tempFile = tempDir.resolve(fileName);
        try (InputStream in = url.openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile.toFile();
    }
}
