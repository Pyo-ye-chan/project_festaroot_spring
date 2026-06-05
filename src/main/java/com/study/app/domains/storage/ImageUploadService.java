package com.study.app.domains.storage;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

@Service
public class ImageUploadService {

    @Value("${gcp.bucket-name}")
    private String bucketName;

    @Autowired
    private Storage storage;

    public String upload(MultipartFile file, String folder) // String folder GCP 내 해당 폴더명
            throws IOException {

        String fileName =
                UUID.randomUUID()
                + "_"
                + file.getOriginalFilename();

        String objectName =
                folder + "/" + fileName;

        BlobId blobId =
                BlobId.of(bucketName, objectName);

        BlobInfo blobInfo =
                BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(
                blobInfo,
                file.getBytes()
        );

        return String.format(
                "https://storage.googleapis.com/%s/%s",
                bucketName,
                objectName
        );
    }
}