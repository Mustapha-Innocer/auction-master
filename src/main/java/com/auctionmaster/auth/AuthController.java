package com.auctionmaster.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auctionmaster.user.User;
import com.auctionmaster.user.UserType;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest newUser) throws Exception{
		User user = new User(
			newUser.getEmail(),
			newUser.getPassword(),
			UserType.USER
		);
		return new ResponseEntity<>(authService.register(user), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public AuthResponse authenticate(@RequestBody AuthRequest loginDetails) throws Exception{
		User user = new User(
			loginDetails.getEmail(),
			loginDetails.getPassword()
		);
		return authService.authenticate(user);
	}
	
}
