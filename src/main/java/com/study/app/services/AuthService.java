package com.study.app.services;

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

import com.study.app.dao.AuthDAO;
import com.study.app.dto.KakaoTokenResponse;
import com.study.app.dto.KakaoUserResponse;
import com.study.app.dto.LoginDTO;
import com.study.app.dto.MemberDTO;
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

//    public String kakaoLogin(String code) {
//        // 1. Get Access Token from Kakao
//        KakaoTokenResponse tokenResponse = getKakaoAccessToken(code);
//        if (tokenResponse == null) {
//            return null; // Failed to get access token
//        }
//
//        // 2. Get User Information from Kakao
//        KakaoUserResponse userResponse = getKakaoUserInfo(tokenResponse.getAccessToken());
//        if (userResponse == null) {
//            return null; // Failed to get user info
//        }
//
//        // 3. User Lookup/Registration
//        String socialId = String.valueOf(userResponse.getId());
//        String socialProvider = "KAKAO";
//        
//        MemberDTO member = authDAO.selectMemberBySocialIdAndProvider(socialId, socialProvider);
//
//        if (member == null) {
//            // User does not exist, register new user
//            member = registerKakaoUser(userResponse);
//            if (member == null) {
//                return null; // Failed to register user
//            }
//        }
//
//        // 4. Generate JWT
//        return jwtUtil.createToken(member.getMember_id());
//    }
//
//    private KakaoTokenResponse getKakaoAccessToken(String code) {
//        String tokenUrl = "https://kauth.kakao.com/oauth/token";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", kakaoClientId);
//        params.add("redirect_uri", kakaoRedirectUri);
//        params.add("code", code);
//        // params.add("client_secret", kakaoClientSecret); // Uncomment if client_secret is required and enabled
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        try {
//            ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(tokenUrl, request, KakaoTokenResponse.class);
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return response.getBody();
//            }
//        } catch (HttpClientErrorException e) {
//            System.out.println("카카오 응답:");
//            System.out.println(e.getResponseBodyAsString());
//            throw e;
//        }
//        
//
//        
//        
//        return null;
//    }

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

    private MemberDTO registerKakaoUser(KakaoUserResponse userResponse) {
        try {
            MemberDTO newMember = new MemberDTO();

            String kakaoId = String.valueOf(userResponse.getId());

            newMember.setMember_id("kakao_" + kakaoId);
            newMember.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
            newMember.setSocial_provider("KAKAO");
            newMember.setSocial_id(kakaoId);

            // 기본 임시값
            newMember.setNickname("kakao_" + kakaoId);
            newMember.setEmail("kakao_" + kakaoId + "@temp.festaroute.local");
            

            // 카카오에서 이메일이 있으면 사용
            if (userResponse.getKakaoAccount() != null) {
                if (userResponse.getKakaoAccount().getEmail() != null) {
                    newMember.setEmail(userResponse.getKakaoAccount().getEmail());
                }

                if (userResponse.getKakaoAccount().getProfile() != null) {
                    newMember.setProfile_image_url(
                        userResponse.getKakaoAccount().getProfile().getProfileImageUrl()
                    );
                }
            }

            newMember.setName("카카오사용자");
            newMember.setPhone("N/A");
            newMember.setGender("M");
            newMember.setBirthdate(LocalDate.of(1900, 1, 1));
            newMember.setAddr_sido("N/A");
            newMember.setAddr_sigungu("N/A");
            newMember.setReside_area_code("N/A");
            newMember.setReside_sigungu_code("N/A");
            newMember.setProfile_image_url("N/A");

            newMember.setExp_point(0L);
            newMember.setTitle_id(1L);
            newMember.setCreated_at(LocalDateTime.now());

            authDAO.insertMember(newMember);

            return newMember;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
