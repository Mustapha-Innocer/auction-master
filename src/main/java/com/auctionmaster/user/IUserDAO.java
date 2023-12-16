package com.auctionmaster.user;

import java.util.Optional;

public interface IUserDAO {
	
	Optional<User> getUserByEmail(String email);

	User saveUser(User user);

	void deleteUser(User user);
}
