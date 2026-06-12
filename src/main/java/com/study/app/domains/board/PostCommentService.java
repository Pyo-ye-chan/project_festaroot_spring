package com.study.app.domains.board;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.app.domains.board.dto.PostCommentDTO;

@Service
public class PostCommentService {
	
	@Autowired
    private PostCommentDAO commentDAO;

    public PostCommentService(PostCommentDAO commentDAO) {
        this.commentDAO = commentDAO;
    }

    public int addComment(PostCommentDTO dto) {
        return commentDAO.insertComment(dto);
    }

    public List<PostCommentDTO> getComments(Long post_id) {

        List<PostCommentDTO> comments =
            commentDAO.selectCommentsByPostId(post_id);

        Map<Long, PostCommentDTO> map = new LinkedHashMap<>();
        List<PostCommentDTO> result = new ArrayList<>();

        for (PostCommentDTO comment : comments) {
            comment.setChildren(new ArrayList<>());
            map.put(comment.getComment_id(), comment);
        }

        for (PostCommentDTO comment : comments) {
            Long parentId = comment.getParent_comment_id();

            if (parentId == null) {
                result.add(comment);
            } else {
                PostCommentDTO parent = map.get(parentId);

                if (parent != null) {
                    parent.getChildren().add(comment);
                }
            }
        }

        return result;
    }

    public int updateComment(PostCommentDTO dto) {
        return commentDAO.updateComment(dto);
    }

    public int deleteComment(Long comment_id, String member_id) {
    	commentDAO.deleteChildComments(comment_id);
        return commentDAO.deleteComment(comment_id, member_id);
    }

}
