package com.auctionmaster.user;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
	
	private final IUserService userService;

	@PostMapping
	public User registUser(@RequestBody User NewUser) {
		return userService.createUser(NewUser);
	}
	
}
