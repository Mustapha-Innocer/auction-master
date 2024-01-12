package com.auctionmaster.userprofile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.time.LocalDate;
import java.util.Optional;

import com.auctionmaster.config.JwtService;
import com.auctionmaster.exception.DuplicateResourceException;
import com.auctionmaster.exception.ResourceNotFoundException;
import com.auctionmaster.user.User;
import com.auctionmaster.user.UserService;
import com.auctionmaster.user.UserType;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

	@Mock
	private UserService userService;
	
	@Mock
	private UserProfileRepository userProfileRepository;

	@Mock
	private JwtService jwtService;

	@Mock
	private HttpServletRequest req;

	@InjectMocks
	private UserProfileService underTest;

	@Captor
	private ArgumentCaptor<User> userCaptor;


	@Test
	void testGetNonExistingProfile() {
		// Given
		User user = new User(
				"test@gmail.com",
				"12345",
				UserType.ADMIN,
				true,
				true,
				true,
				true);
		
		// When
		given(jwtService.extractUsername(req)).willReturn("test@gmail.com");
		given(userService.retrieveUserByEmail("test@gmail.com")).willReturn(user);

		// Then
		assertThatThrownBy(() -> underTest.getUserProfile(req))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("No profile found.");
	}
	
	@Test
	void testGetExistingProfile() {
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
		
		// When
		given(jwtService.extractUsername(req)).willReturn("test@gmail.com");
		given(userService.retrieveUserByEmail("test@gmail.com")).willReturn(user);

		// Then
		assertThat(underTest.getUserProfile(req)).isEqualTo(profile);
	}

	@Test
	void testCreateProfile() {
		// Given
		User user = new User(
				"test@gmail.com",
				"12345",
				UserType.ADMIN,
				true,
				true,
				true,
				true);
		
		User expectedUser = new User(
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
		
		// When
		given(jwtService.extractUsername(req)).willReturn("test@gmail.com");
		given(userService.retrieveUserByEmail("test@gmail.com")).willReturn(user);
		given(userProfileRepository.findByUsername("Username")).willReturn(Optional.empty());
		given(userProfileRepository.findByContactNumber("+233544444444")).willReturn(Optional.empty());

		// Then
		underTest.createProfile(req, profile);
		then(userService).should().saveUser(userCaptor.capture());
		expectedUser.setProfile(profile);
		assertThat(userCaptor.getValue()).isEqualTo(expectedUser);
	}

	@Test
	void testCreateExistingProfile() {
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
		
		// When
		given(jwtService.extractUsername(req)).willReturn("test@gmail.com");
		given(userService.retrieveUserByEmail("test@gmail.com")).willReturn(user);
		
		// Then
		assertThatThrownBy(()->underTest.createProfile(req, profile))
			.isInstanceOf(DuplicateResourceException.class)
			.hasMessage("Profile already exist, try update.");
		
		then(userService).should(never()).saveUser(any(User.class));
	}
	
	@Test
	void testCreateProfileWithExistingUsername() {
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
		
		// When
		given(jwtService.extractUsername(req)).willReturn("test@gmail.com");
		given(userService.retrieveUserByEmail("test@gmail.com")).willReturn(user);
		given(userProfileRepository.findByUsername("Username")).willReturn(Optional.of(profile));
		// given(userProfileRepository.findByContactNumber("+233544444444")).willReturn(Optional.empty());

		// Then
		assertThatThrownBy(()->underTest.createProfile(req, profile))
			.isInstanceOf(DuplicateResourceException.class)
			.hasMessage("Username is already taken.");
		
		then(userService).should(never()).saveUser(any(User.class));
	}
	
	@Test
	void testCreateProfileWithExistingContactNumber() {
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
		
		// When
		given(jwtService.extractUsername(req)).willReturn("test@gmail.com");
		given(userService.retrieveUserByEmail("test@gmail.com")).willReturn(user);
		given(userProfileRepository.findByUsername("Username")).willReturn(Optional.empty());
		given(userProfileRepository.findByContactNumber("+233544444444")).willReturn(Optional.of(profile));

		// Then
		assertThatThrownBy(()->underTest.createProfile(req, profile))
			.isInstanceOf(DuplicateResourceException.class)
			.hasMessage("+233544444444 is already taken.");
		
		then(userService).should(never()).saveUser(any(User.class));
	}
}
