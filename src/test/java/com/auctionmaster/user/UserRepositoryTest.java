package com.auctionmaster.user;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository underTest;

	@Test
	void testFindUserByEmail() {
		// Given
		User testUser = new User(
				"test@gmail.com",
				"hash", 
				UserType.ADMIN, 
				true, 
				true, 
				true, 
				true);
		underTest.save(testUser);

		// When
		Optional<User> user = underTest.findUserByEmail("test@gmail.com");

		// Then
		Assertions.assertThat(user).isPresent();
	}
}
