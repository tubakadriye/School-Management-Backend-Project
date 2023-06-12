package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {


	public final JwtUtils jwtUtils;
	public final AuthenticationManager authenticationManager;

	public ResponseEntity<>authenticateUser(){
		return null;
	}
}
