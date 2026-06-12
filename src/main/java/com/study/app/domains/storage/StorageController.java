package com.study.app.domains.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.study.app.domains.board.FileDAO;
import com.study.app.domains.board.dto.PostAttachmentDTO;

@RestController
@RequestMapping("/storage")
public class StorageController {
	
	@Autowired
	private Storage storage;
	
    @Autowired
    private uploadService uploadService;
    
    @Value("${gcp.bucket-name}")
    private String bucketName;
    
    @Autowired
    private FileDAO fileDAO;

    
    @PostMapping("/board/image")
    public String uploadImage(
    		@RequestParam("file") MultipartFile file,
    		@RequestParam("folder") String folder
    		)throws IOException {
    	
        return uploadService.upload(file, folder);
    }
    
    @PostMapping("/board/file")
    public String uploadFile(
    		@RequestParam("file") MultipartFile file,
    		@RequestParam("folder") String folder
    		)throws IOException{
    
    	return uploadService.upload(file, folder); 
    	
    }
    
    @GetMapping("/download/{attach_id}")
    public ResponseEntity<Resource> download(@PathVariable Long attach_id) throws Exception {

        PostAttachmentDTO file = fileDAO.selectPostAttachById(attach_id);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        
        String objectName =
        	    file.getFile_path()
        	        .replace(
        	            "https://storage.googleapis.com/"
        	            + bucketName
        	            + "/",
        	            ""
        	        );

        Blob blob = storage.get(
            BlobId.of(bucketName, objectName)
        );

        if (blob == null || !blob.exists()) {
            return ResponseEntity.notFound().build();
        }

        String fileName = URLEncoder.encode(
            file.getFile_name(),
            StandardCharsets.UTF_8
        ).replaceAll("\\+", "%20");

        Resource resource = new InputStreamResource(
            new ByteArrayInputStream(blob.getContent())
        );

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + fileName)
            .header(HttpHeaders.CONTENT_TYPE, blob.getContentType())
            .body(resource);
    }

}
