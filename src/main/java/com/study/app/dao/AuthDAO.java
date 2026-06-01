package com.study.app.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.app.dto.LoginDTO;
import com.study.app.dto.MemberDTO;

@Mapper
public interface AuthDAO {
    MemberDTO selectMemberById(String member_id);
    MemberDTO selectMemberBySocialIdAndProvider(@Param("socialId") String socialId, @Param("socialProvider") String socialProvider);
    void insertMember(MemberDTO memberDTO);
}
