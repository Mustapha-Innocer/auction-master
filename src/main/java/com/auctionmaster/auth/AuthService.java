package com.auctionmaster.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auctionmaster.config.JwtService;
import com.auctionmaster.exception.DuplicateResourceException;
import com.auctionmaster.token.Token;
import com.auctionmaster.token.TokenRepository;
import com.auctionmaster.token.TokenType;
import com.auctionmaster.user.User;
import com.auctionmaster.user.UserDAO;
import com.auctionmaster.user.UserType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UserDAO userDAO;
	private final TokenRepository tokenRepository;

	public AuthResponse register(User newUser) {

		log.info("Creating new user.");

		Optional<User> user = userDAO.getUserByEmail(newUser.getEmail());

		user.ifPresent(
				u -> {
					throw new DuplicateResourceException(
							"%s already exist".formatted(u.getEmail()));
				});

		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setRole(UserType.ADMIN);
		AuthUser authUser = new AuthUser(
				newUser.getEmail(),
				newUser.getPassword(),
				newUser.getRole().getAuthorities(),
				true,
				true,
				true,
				true);

		userDAO.saveUser(newUser);
		String token = jwtService.generateToken(authUser);
		String refreshToken = jwtService.generateRefreshToken(authUser);

		tokenRepository.saveAll(
				List.of(
						new Token(token, newUser, TokenType.BEARER, false, false),
						new Token(refreshToken, newUser, TokenType.BEARER, false, false)));
		
		log.info("New user created successfully.");

		return new AuthResponse(
				token,
				refreshToken);
	}

}
