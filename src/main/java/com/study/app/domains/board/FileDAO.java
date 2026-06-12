package com.study.app.domains.board;

import java.util.List;
import java.util.UUID;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.study.app.domains.board.dto.PostAttachmentDTO;
import com.study.app.domains.storage.StorageController;

@Repository
public class FileDAO {

	@Autowired
	private SqlSessionTemplate mybatis;

	@Autowired
	private Storage storage;

	@Value("${gcp.bucket-name}")
	private String bucketName;

	public void insertPostAttachments(Long post_id, List<MultipartFile> files) {

		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				try {
					String originalName = file.getOriginalFilename();

					String sysName = "board/file/" + UUID.randomUUID().toString() + "_" + originalName;

					BlobId blobId = BlobId.of(bucketName, sysName);

					BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

					storage.create(blobInfo, file.getBytes());

					PostAttachmentDTO attachmentDTO = new PostAttachmentDTO();

					mybatis.insert("PostAttachment.insertFiles", attachmentDTO);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deletePostAttachByPostId(Long post_id) {
		mybatis.delete("PostAttachment.dedeletePostAttachByPostId", post_id);
	}

	public List<PostAttachmentDTO> selectPostAttachByPostId(Long post_id) {

		// 첨부파일 목록 조회 시
		List<PostAttachmentDTO> files = mybatis.selectList("PostAttachment.selectPostAttachByPostId", post_id);

		for (PostAttachmentDTO file : files) { // 조회 시 전체 경로 완성
			String path = file.getFile_path();

			if (path != null && !path.startsWith("http")) {
				file.setFile_path(
						"https://storage.googleapis.com/"
								+ bucketName
								+ "/"
								+ path
						);
			}
		}

		return files;

	}

	public void deletePostAttachById(Long id) {
		mybatis.delete("PostAttachment.dedeletePostAttachById", id);
	}

	public PostAttachmentDTO selectPostAttachById(Long id) {
		return mybatis.selectOne("PostAttachment.selectPostAttachById", id);
	}

}
