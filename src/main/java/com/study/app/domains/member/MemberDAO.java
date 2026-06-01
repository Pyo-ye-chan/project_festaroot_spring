package com.study.app.domains.member;

import org.apache.ibatis.annotations.Mapper;

import com.study.app.domains.member.dto.MemberDTO;

@Mapper
public interface MemberDAO {
    int insertMember(MemberDTO memberDTO);

}
