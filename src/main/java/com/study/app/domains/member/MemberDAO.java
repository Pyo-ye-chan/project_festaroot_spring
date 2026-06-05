package com.study.app.domains.member;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.study.app.domains.member.dto.InterestRegionDTO;
import com.study.app.domains.member.dto.InterestThemeDTO;
import com.study.app.domains.member.dto.MemberDTO;

@Mapper
public interface MemberDAO {
    int insertMember(MemberDTO memberDTO);

    MemberDTO selectMemberById(String member_id);
    List<com.study.app.domains.member.dto.InterestRegionDTO> selectInterestRegions(String member_id);
    List<com.study.app.domains.member.dto.InterestThemeDTO> selectInterestThemes(String member_id);

    
    int insertInterestTheme(InterestThemeDTO dto);

    int insertInterestRegion(InterestRegionDTO dto);
    
    MemberDTO getProfile(String member_id);

}
