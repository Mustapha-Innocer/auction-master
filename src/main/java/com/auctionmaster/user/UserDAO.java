package com.auctionmaster.user;

import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDAO implements IUserDAO{
	
	private final UserRepository userRepository;

	@Override
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}
	
}
