package com.auctionmaster.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auctionmaster.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

	private final IUserDAO userDAO;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User saveUser(User user) {
		if (user.getId()==null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return userDAO.saveUser(user);
	}

	@Override
	public User retrieveUserByEmail(String email) {
		return userDAO.getUserByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(
						"User with email %s not found".formatted(email)));
	}
}
