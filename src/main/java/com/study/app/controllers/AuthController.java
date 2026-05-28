package com.study.app.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.app.dto.LoginDTO;
import com.study.app.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	public AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {

	    String token = authService.login(loginDTO);

	    Map<String, Object> result = new HashMap<>();

	    if (token != null) {

	        result.put("success", true);
	        result.put("message", loginDTO.getMember_id() + "님, 환영합니다!");
	        result.put("token", token);

	        return ResponseEntity.ok(result);

	    } else {

	        result.put("success", false);
	        result.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
	    }
	}
}
