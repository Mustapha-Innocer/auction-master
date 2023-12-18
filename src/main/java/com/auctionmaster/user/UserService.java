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
	public User createUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(UserType.BIDDER);
		return userDAO.saveUser(user);
	}

	@Override
	public User retrieveUserByEmail(String email) {
		return userDAO.getUserByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(
						"User with email %s not found".formatted(email)));
	}

	@Override
	public void removeUserByEmail(String email) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'removeUserByEmail'");
	}

	@Override
	public User changePassword(String email, User upadates) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
	}

}
