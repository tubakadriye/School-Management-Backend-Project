package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.LoginRequest;
import com.project.schoolmanagment.payload.response.AuthResponse;
import com.project.schoolmanagment.service.user.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse>authenticateUser(@RequestBody @Valid LoginRequest loginRequest){
		return authenticationService.authenticateUser(loginRequest);
	}
}
