package com.study.app.domains.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.app.domains.board.dto.PostCommentDTO;

@Repository
public class PostCommentDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
    
    public int insertComment(PostCommentDTO dto) {
    	return mybatis.insert("PostComment.insertComment", dto);
    }
    
    public List<PostCommentDTO> selectCommentsByPostId(Long post_id) {
    	return mybatis.selectList("PostComment.selectCommentsByPostId", post_id);
    }
    
    public int updateComment(PostCommentDTO dto) {
    	return mybatis.update("PostComment.updateComment", dto);
    }
    
    public int deleteComment(Long comment_id, String member_id) {
        Map<String, Object> map = new HashMap<>();

        map.put("comment_id", comment_id);
        map.put("member_id", member_id);
        

        return mybatis.delete("PostComment.deleteComment", map);
    }
    
    public int deleteChildComments(Long comment_id) {
    	return mybatis.delete("PostComment.deleteChildComments", comment_id);
    }

    
}
