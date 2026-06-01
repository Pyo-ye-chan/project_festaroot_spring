package com.study.app.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.app.services.AuthService;


@RestController
@RequestMapping("/oauth")
public class OauthController {
	
	@Autowired
	public AuthService authService;
	
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<Map<String, Object>> kakaoCallback(
	        @RequestParam String code) {

	    Map<String, Object> result =
	        authService.kakaoLogin(code);
	   

	    return ResponseEntity.ok(result);
	}
}
