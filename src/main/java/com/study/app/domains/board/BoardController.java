package com.study.app.domains.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.app.domains.board.dto.CommunityPostDTO;
import com.study.app.domains.board.dto.PostAttachmentDTO;
import com.study.app.domains.board.dto.PostCommentDTO;
import com.study.app.utils.JWTUtil;

@RestController
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private JWTUtil jwt;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private PostCommentService commentService;
	
	@PostMapping("/post")
	public ResponseEntity<Void> addPost(
			@RequestPart("post") CommunityPostDTO dto, 
			@RequestPart(value = "files", required = false) List<MultipartFile> files,
			@RequestHeader("Authorization") String authHeader){
		
		String token = authHeader.replace("Bearer ", "");
		String member_id = jwt.getSubject(token);
		
		dto.setMember_id(member_id);
		
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
			
		return ResponseEntity.ok(resp);	
	}
	
	@GetMapping("/post/{id}")
	public ResponseEntity<Map<String, Object>> getPostDetail(@PathVariable Long id) {
		CommunityPostDTO dto = boardService.getPostDetail(id);
		List<PostAttachmentDTO> list = boardService.getPostAttachList(id);
		
		Map<String, Object> resp = new HashMap<>();
		resp.put("dto", dto);
		resp.put("list", list);
		
		return ResponseEntity.ok(resp);
	}
	
	@PutMapping("/post/{id}")
	public ResponseEntity<Void> updatePost(
			@PathVariable Long id,
			@RequestPart("post") CommunityPostDTO dto,
			@RequestPart(value = "files", required = false) List<MultipartFile> files){
		dto.setPost_id(id);
		boardService.updatePost(dto, files);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/post/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long id) {
		boardService.deletePost(id);
		return ResponseEntity.ok().build();
	}

    // 댓글 / 대댓글 작성
    @PostMapping("/posts/{post_id}/comments")
    public ResponseEntity<Void> addComment(
            @PathVariable Long post_id,
            @RequestBody PostCommentDTO dto,
            @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.replace("Bearer ", "");
        String member_id = jwt.getSubject(token);

        dto.setPost_id(post_id);
        dto.setMember_id(member_id);

        int result = commentService.addComment(dto);

        if (result > 0) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    // 댓글 목록 조회
    @GetMapping("/posts/{post_id}/comments")
    public ResponseEntity<List<PostCommentDTO>> getComments(
            @PathVariable Long post_id
    ) {
        List<PostCommentDTO> comments = commentService.getComments(post_id);

        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/comments/{comment_id}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long comment_id,
            @RequestBody PostCommentDTO dto,
            @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.replace("Bearer ", "");
        String member_id = jwt.getSubject(token);

        dto.setComment_id(comment_id);
        dto.setMember_id(member_id);

        int result = commentService.updateComment(dto);

        if (result > 0) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long comment_id,
            @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.replace("Bearer ", "");
        String member_id = jwt.getSubject(token);

        int result = commentService.deleteComment(comment_id, member_id);

        if (result > 0) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

}
