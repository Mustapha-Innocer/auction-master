package com.auctionmaster.user;

public interface IUserService {

	User saveUser(User user);

	User retrieveUserByEmail(String email);
}
