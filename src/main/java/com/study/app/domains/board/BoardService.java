package com.study.app.domains.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.app.domains.board.dto.CommunityPostDTO;
import com.study.app.domains.board.dto.PostAttachmentDTO;
import com.study.app.domains.storage.uploadService;

@Service
public class BoardService {

	@Autowired
	private PostDAO postDAO;

	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private uploadService uploadService;

	public void addPost(CommunityPostDTO dto, List<MultipartFile> files) {
		postDAO.insertPost(dto);

		if(files != null && !files.isEmpty()) {
			fileDAO.insertPostAttachments(dto.getPost_id(), files);	
		}
	}

	public int totalPostCount() {
		return postDAO.selectCount();
	}

	public List<CommunityPostDTO> getStartEnd(Long startNum, Long endNum){
		return postDAO.selectList(startNum, endNum);
	}

	public CommunityPostDTO getPostDetail(Long id) {
		return postDAO.selectById(id);
	}
	
	public List<PostAttachmentDTO> getPostAttachList(Long id) {
		return fileDAO.selectPostAttachByPostId(id);
	}

	public void updatePost(CommunityPostDTO dto, List<MultipartFile> files) {
		postDAO.updatePostById(dto);
		
	    if (dto.getDeleteFileIds() != null) {
	        for (Long attach_id : dto.getDeleteFileIds()) {
	        	
	        	// 1. DB 삭제
	            fileDAO.deletePostAttachById(attach_id);
	            
	            // 2. GCP 삭제
	            PostAttachmentDTO attachment = fileDAO.selectPostAttachById(attach_id);
	        }
	    }

	    if (files != null && !files.isEmpty()) {
	    	fileDAO.insertPostAttachments(dto.getPost_id(), files);	

	    }
	}

	public void deletePost(Long id) {
		
	    // 1. 게시글 첨부파일 조회
	    List<PostAttachmentDTO> attachments = fileDAO.selectPostAttachByPostId(id);

	    // 2. GCP Storage 실제 파일 삭제
	    for (PostAttachmentDTO file : attachments) {
	        if (file.getFile_path() != null) {
	            uploadService.deleteFile(file.getFile_path());
	        }
	    }

	    // 3. 첨부파일 DB 삭제
	    fileDAO.deletePostAttachByPostId(id);
	    
		postDAO.deletePostById(id);
		
	}
}
