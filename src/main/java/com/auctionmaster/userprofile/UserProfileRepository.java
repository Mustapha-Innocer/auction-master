package com.auctionmaster.userprofile;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
	Optional<UserProfile> findByUsername(String username);
	
	Optional<UserProfile> findByContactNumber(String contactNumber);
}
