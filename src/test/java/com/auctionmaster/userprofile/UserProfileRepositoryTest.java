package com.auctionmaster.userprofile;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.auctionmaster.user.User;
import com.auctionmaster.user.UserRepository;
import com.auctionmaster.user.UserType;

@DataJpaTest
public class UserProfileRepositoryTest {

	@Autowired
	UserProfileRepository underTest;

	@Autowired
	UserRepository userRepository;

	@Test
	void testFindByContactNumber() {
		// Given
		User user = new User(
				"test@gmail.com",
				"12345",
				UserType.ADMIN,
				true,
				true,
				true,
				true);
		
		UserProfile profile = new UserProfile(
			user, 
			"Username", 
			"Firstname", 
			"Lastname", 
			"+233544444444", 
			null, 
			LocalDate.of(1994, 12, 5));
		
		user.setProfile(profile);
		userRepository.save(user);

		// When
		Optional<UserProfile> actualProfile = underTest.findByUsername("Username"); 

		// Then
		assertThat(actualProfile).isPresent();

	}

	@Test
	void testFindByUsername() {
		// Given
		User user = new User(
				"test@gmail.com",
				"12345",
				UserType.ADMIN,
				true,
				true,
				true,
				true);
		
		UserProfile profile = new UserProfile(
			user, 
			"Username", 
			"Firstname", 
			"Lastname", 
			"+233123456789", 
			null, 
			LocalDate.of(1994, 12, 5));
		
		user.setProfile(profile);
		userRepository.save(user);

		// When
		Optional<UserProfile> actualProfile = underTest.findByContactNumber("+233123456789"); 

		// Then
		assertThat(actualProfile).isPresent();
	}
}
