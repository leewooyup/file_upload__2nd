package com.ll.exam.app.fileupload.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/upload")
public class FileUploadController {
    @Value("${custom.genFileDirPath}")
    private String genFIleDirPath;

    @RequestMapping("")
    @ResponseBody
    public String upload(@RequestParam("img1") MultipartFile img1, @RequestParam("img2") MultipartFile img2) {
        try {
            img1.transferTo(new File(genFIleDirPath + "/1.png"));
            img2.transferTo(new File(genFIleDirPath + "/2.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "업로드 완료!";
    }
}
