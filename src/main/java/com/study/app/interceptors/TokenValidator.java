package com.study.app.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.study.app.utils.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenValidator implements HandlerInterceptor {
	
	@Autowired
	private JWTUtil jwt;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("리퀘스트: " + request);

		if(request.getMethod().equalsIgnoreCase("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
			return true;
		}

		String authHeader = request.getHeader("Authorization");

		System.out.println("authHeader: " + authHeader);

		String token;

		if(authHeader != null && authHeader.startsWith("Bearer ")) {

			token = authHeader.substring(7);

		}else {
			token = request.getParameter("token");
		}

		System.out.println("token: " + token);

		if(token != null) {
			try {
				String id = jwt.getSubject(token);
				request.setAttribute("id", id);
				return true;

			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return false; // 토큰이 애초에 없거나 Bearer로 시작하지 않는다면

	}
}
