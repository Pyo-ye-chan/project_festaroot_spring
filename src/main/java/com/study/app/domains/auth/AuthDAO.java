package com.study.app.domains.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.app.domains.auth.dto.LoginDTO;
import com.study.app.domains.member.dto.MemberDTO;

@Mapper
public interface AuthDAO {
    MemberDTO selectMemberById(String member_id);
    MemberDTO selectMemberBySocialIdAndProvider(@Param("socialId") String socialId, @Param("socialProvider") String socialProvider);
}
