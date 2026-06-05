package com.study.app.domains.storage;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
public class StorageTestController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/test")
    public String uploadTest(@RequestParam("file") MultipartFile file) throws IOException {
        return imageUploadService.upload(file, "reviews");
    }
}