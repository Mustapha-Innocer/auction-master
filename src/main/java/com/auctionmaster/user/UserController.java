package com.auctionmaster.user;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
	
	private final IUserService userService;

	@GetMapping("{email}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public User retrieveAllUser(@PathVariable("email") String email) {
		return userService.retrieveUserByEmail(email);
	}
	
}
