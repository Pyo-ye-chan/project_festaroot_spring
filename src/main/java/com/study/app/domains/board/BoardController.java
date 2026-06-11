package com.study.app.domains.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.app.domains.board.dto.CommunityPostDTO;
import com.study.app.utils.JWTUtil;

@RestController
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private JWTUtil jwt;
	
	@Autowired
	private BoardService boardService;
	
	@PostMapping("/post")
	public ResponseEntity<Void> addPost(
			@RequestPart("post") CommunityPostDTO dto, 
			@RequestPart(value = "files", required = false) List<MultipartFile> files,
			@RequestHeader("Authorization") String authHeader){
		
		String token = authHeader.replace("Bearer ", "");
		String member_id = jwt.getSubject(token);
		
		dto.setMember_id(member_id);
		
		System.out.println(dto);
		System.out.println(files);
		
		boardService.addPost(dto, files);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/posts")
	public ResponseEntity<Map<String, Object>> getPostList(
			@RequestParam(defaultValue = "1") Long cpage) {
		int totalPostCount = boardService.totalPostCount();
		
		Long startNum = cpage*10-9;
		Long endNum = cpage*10;
		
		List<CommunityPostDTO> list = boardService.getStartEnd(startNum, endNum);
			
		Map<String, Object> resp = new HashMap<>();
		
		resp.put("list", list);
		resp.put("totalPostCount", totalPostCount);
		
		String nick = list.get(0).getNickname();
		System.out.println(nick);
			
		return ResponseEntity.ok(resp);	
	}
	
	@GetMapping("/post/{id}")
	public ResponseEntity<CommunityPostDTO> getPostDetail(@PathVariable Long id) {
		CommunityPostDTO dto = boardService.getPostDetail(id);
		return ResponseEntity.ok(dto);
	}
	
	
	
	

}
