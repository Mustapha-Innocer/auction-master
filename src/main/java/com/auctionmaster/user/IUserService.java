package com.auctionmaster.user;

public interface IUserService {
	
	User createUser(User user);

	User retrieveUserByEmail(String email);

	User changePassword(String email, User upadates);

	void removeUserByEmail(String email);
}
