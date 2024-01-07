package com.auctionmaster.userprofile;

import org.springframework.stereotype.Service;

import com.auctionmaster.config.JwtService;
import com.auctionmaster.exception.ResourceNotFoundException;
import com.auctionmaster.user.User;
import com.auctionmaster.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
@Service
public class UserProfileService {

	private final JwtService jwtService;
	private final UserService userService;

	public UserProfile getUserProfile(HttpServletRequest req) {
		String userEmail = jwtService.extractUsername(req);
		User user = userService.retrieveUserByEmail(userEmail);
		if (user.getProfile() == null) {
			throw new ResourceNotFoundException("No profile found.");
		}
		return user.getProfile();
	}
	
}
