package com.study.app.domains.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.app.domains.board.dto.CommunityPostDTO;

@Service
public class BoardService {
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileDAO fileDAO;
	
	public void addPost(CommunityPostDTO dto, List<MultipartFile> files) {
		postDAO.insertPost(dto);
		
		Long boardSeq = dto.getPost_id();
		
		if(files != null && !files.isEmpty()) {
			fileDAO.insertPostAttachments(boardSeq, files);	
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

}
