package com.study.app.domains.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import BCryptPasswordEncoder

import com.study.app.domains.auth.dto.LoginDTO;
import com.study.app.domains.member.dto.MemberDTO;
import com.study.app.utils.JWTUtil;
// import com.study.app.utils.EncryptionUtil; // Remove EncryptionUtil import

@Service
public class MemberService {

    @Autowired
    private MemberDAO memberDAO;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; // Inject BCryptPasswordEncoder

    public int signup(MemberDTO memberDTO) {
    	
    	// Encrypt password before saving
    	memberDTO.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword())); // Use BCrypt for encoding
    	
    	if(memberDTO.getReside_area_code() == null) {
    	    memberDTO.setReside_area_code("0");
    	}
    	
    	if(memberDTO.getReside_sigungu_code() == null) {
    	    memberDTO.setReside_sigungu_code("0");
    	}
    	
    	if(memberDTO.getSocial_provider() == null) {
    	    memberDTO.setSocial_provider("LOCAL");
    	}

    	if(memberDTO.getProfile_image_url() == null) {
    	    memberDTO.setProfile_image_url("");
    	}

    	if(memberDTO.getTitle_id() == null) {
    	    memberDTO.setTitle_id(1L);
    	}
    	
        return memberDAO.insertMember(memberDTO);
    }

}
