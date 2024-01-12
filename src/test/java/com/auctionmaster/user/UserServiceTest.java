package com.auctionmaster.user;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.auctionmaster.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserDAO userDAO;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService underTest;


	@Test
	void testSaveUser() {

		// Given
		User user = new User(
				"test@gmail.com",
				"12345",
				UserType.ADMIN,
				true,
				true,
				true,
				true);

		given(passwordEncoder.encode("12345")).willReturn("password");
		User userReturn = new User(
				"test@gmail.com",
				"password",
				UserType.ADMIN,
				true,
				true,
				true,
				true);
		given(userDAO.saveUser(user)).willReturn(user);

		// When
		User res = underTest.saveUser(user);

		// Then
		assertThat(res).isEqualTo(userReturn);
	}

	@Test
	void testUpdateUser() {
		// Given
		User user = new User(
				"test@gmail.com",
				"12345",
				UserType.ADMIN,
				true,
				true,
				true,
				true);
		user.setId(1L);
		
		User expectedUser = new User(
				"test@gmail.com",
				"12345",
				UserType.ADMIN,
				true,
				true,
				true,
				true);
		expectedUser.setId(1L);
		
		given(userDAO.saveUser(user)).willReturn(user);

		// When
		User actualUser = underTest.saveUser(user);
		
		// Then
		assertThat(actualUser).isEqualTo(expectedUser);
	}

	@Test
	void testRetrieveUserByEmail() {
		// Given
		User expecUser = new User(
				"test@gmail.com",
				"12345",
				UserType.ADMIN,
				true,
				true,
				true,
				true);
		given(userDAO.getUserByEmail("test@gmail.com"))
				.willReturn(Optional.of(expecUser));

		// When
		User actualUser = underTest.retrieveUserByEmail("test@gmail.com");

		// Then
		assertThat(actualUser).isEqualTo(expecUser);
	}

	@Test
	void testRetrieveNonExistingUserByEmail() {
		// Given
		given(userDAO.getUserByEmail("test@gmail.com"))
				.willReturn(Optional.empty());

		// Then
		assertThatThrownBy(() -> underTest.retrieveUserByEmail("test@gmail.com"))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("User with email test@gmail.com not found");
	}
}
