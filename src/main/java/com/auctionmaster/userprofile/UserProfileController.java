package com.auctionmaster.userprofile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
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
	
	// TODO: Create profile

	// TODO: update profile
}
