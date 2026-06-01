package com.study.app.domains.auth;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.study.app.domains.auth.dto.LoginDTO;
import com.study.app.domains.member.dto.MemberDTO;
import com.study.app.domains.oauth.KakaoTokenResponse;
import com.study.app.domains.oauth.KakaoUserResponse;
import com.study.app.utils.JWTUtil;

@Service
public class AuthService {

    @Autowired
    private AuthDAO authDAO;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    
    
    public String login(LoginDTO loginDTO) {
        MemberDTO storedMember = authDAO.selectMemberById(loginDTO.getMember_id());
        
        if (storedMember != null) {
        	// Use BCryptPasswordEncoder.matches() to compare passwords
        	if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), storedMember.getPassword())) {
        		// Passwords match, generate JWT token
        		return jwtUtil.createToken(storedMember.getMember_id());
        	}
        }
        return null; // Login failed
    }
    
    public Map<String, Object> kakaoLogin(String code) {

        Map<String, Object> result = new HashMap<>();

        KakaoTokenResponse tokenResponse = getKakaoAccessToken(code);
        

        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            result.put("success", false);
            result.put("message", "카카오 access token 발급 실패");
            return result;
        }

        KakaoUserResponse userResponse =
            getKakaoUserInfo(tokenResponse.getAccessToken());

        if (userResponse == null) {
            result.put("success", false);
            result.put("message", "카카오 사용자 정보 조회 실패");
            return result;
        }

        String socialId = String.valueOf(userResponse.getId());
        String socialProvider = "KAKAO";

        MemberDTO member = authDAO.selectMemberBySocialIdAndProvider(
            socialId,
            socialProvider
        );

        // 기존 회원
        if (member != null) {
            String token = jwtUtil.createToken(member.getMember_id());

            result.put("success", true);
            result.put("isNewUser", false);
            result.put("token", token);
            result.put("member_id", member.getMember_id());
            result.put("nickname", member.getNickname());
            result.put("email", member.getEmail());
            result.put("social_provider", socialProvider);

            return result;
        }

        // 신규 회원
        String generatedMemberId = "kakao_" + socialId;
        
        System.out.println(generatedMemberId);

        result.put("member_id", generatedMemberId);
        result.put("social_provider", socialProvider);
        result.put("social_id", socialId);
        
        result.put("success", true);
        result.put("isNewUser", true);
        result.put("social_provider", socialProvider);
        result.put("social_id", socialId);
        result.put("email", "");
        result.put("nickname", "");
        result.put("name", "");
        result.put("profile_image_url", "");

        return result;
    }

    private KakaoTokenResponse getKakaoAccessToken(String code) {

        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request =
            new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response =
            restTemplate.postForEntity(
                tokenUrl,
                request,
                KakaoTokenResponse.class
            );

        return response.getBody();
    }


    private KakaoUserResponse getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);
        
        

        try {
            ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, KakaoUserResponse.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            // Log exception
            e.printStackTrace();
        }
        return null;
    }

}
