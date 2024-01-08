package com.auctionmaster.userprofile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;

@Data
@RequestMapping("/api/v1/profile")
@RestController
public class UserProfileController {

	private final UserProfileService userProfileService;

	@GetMapping
	public ResponseEntity<UserProfile> createProfile(HttpServletRequest req) {
		return new ResponseEntity<>(userProfileService.getUserProfile(req), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<UserProfile> createProfile(HttpServletRequest req, @Valid @RequestBody UserProfile userProfile) {
		userProfileService.createProfile(req, userProfile);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// TODO: update profile
}
