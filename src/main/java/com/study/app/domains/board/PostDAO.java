package com.study.app.domains.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.cloud.storage.Storage;
import com.study.app.domains.board.dto.CommunityPostDTO;

@Repository
public class PostDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	@Autowired
	private Storage storage;
	
	public void insertPost(CommunityPostDTO dto) {
		mybatis.insert("Board.insertPost", dto);
	}
	
	public int selectCount() {
		return mybatis.selectOne("Board.selectCount");
	}
	
	public List<CommunityPostDTO> selectList(Long startNum, Long endNum) {
		Map<String, Long> resp = new HashMap<>();
		resp.put("startNum", startNum);
		resp.put("endNum", endNum);
		
		return mybatis.selectList("Board.selectList", resp);
	}
	
	public CommunityPostDTO selectById(Long id) {
		return mybatis.selectOne("Board.selectById", id);
	}

}
