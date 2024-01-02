package com.auctionmaster.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auctionmaster.user.User;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public AuthResponse register(@RequestBody User newUser) throws Exception{
		return authService.register(newUser);
	}

	@PostMapping("/login")
	public AuthResponse authenticate(@RequestBody User user) throws Exception{
		return authService.authenticate(user);
	}
	
}
