package com.ll.exam.app.util;


import org.apache.tika.Tika;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public class Util {
    public static class date {
        public static String getCurrentDateFormatted(String pattern) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            return date;
        }
    }
    public static class file {
        public static String getExt(String filename) {
            return Optional.ofNullable(filename)
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(filename.lastIndexOf(".")+1))
                    .orElse("");
        }


        public static String downloading(String url, String filePath) {
            new File(filePath).getParentFile().mkdirs();

            byte[] imageBytes = new RestTemplate().getForObject(url, byte[].class); // 파일 받아오기
            try {
                Files.write(Paths.get(filePath), imageBytes); // 저장
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String mimeType = null;
            try {
                mimeType = new Tika().detect(new File(filePath)); // 외부라이브러리 Tika, 확장자 detect
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String ext = mimeType.replaceAll("image/", "");
            ext = ext.replaceAll("jpeg", "jpg");

            String newFilePath = filePath + "." + ext;

            new File(filePath).renameTo(new File(newFilePath));

            return newFilePath;
        }
    }
}
