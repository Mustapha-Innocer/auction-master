package com.auctionmaster.userprofile;

import org.springframework.stereotype.Service;

import com.auctionmaster.config.JwtService;
import com.auctionmaster.exception.DuplicateResourceException;
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
	private final UserProfileRepository userProfileRepository;

	private User getUser(HttpServletRequest req) {
		String userEmail = jwtService.extractUsername(req);
		return userService.retrieveUserByEmail(userEmail);
	}

	public UserProfile getUserProfile(HttpServletRequest req) {
		User user = getUser(req);
		if (user.getProfile() == null) {
			throw new ResourceNotFoundException("No profile found.");
		}
		return user.getProfile();
	}

	public void createProfile(HttpServletRequest req, UserProfile userProfile) {

		User user = getUser(req);
		if (user.getProfile() != null) {
			throw new DuplicateResourceException("Profile already exist, try update.");
		}

		userProfileRepository.findByUsername(userProfile.getUsername())
				.ifPresent((p) -> {
					throw new DuplicateResourceException("%s is already taken.".formatted(p.getUsername()));
				});
		
		userProfileRepository.findByContactNumber(userProfile.getContactNumber())
				.ifPresent((p) -> {
					throw new DuplicateResourceException("%s is already taken.".formatted(p.getContactNumber()));
				});

		userProfile.setUser(user);
		user.setProfile(userProfile);
		userService.saveUser(user);
	}

}
