package com.auctionmaster.auth;

import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.auctionmaster.config.JwtService;
import com.auctionmaster.exception.DuplicateResourceException;
import com.auctionmaster.token.Token;
import com.auctionmaster.token.TokenRepository;
import com.auctionmaster.token.TokenType;
import com.auctionmaster.user.User;
import com.auctionmaster.user.UserDAO;
import com.auctionmaster.user.UserService;
import com.auctionmaster.user.UserType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final JwtService jwtService;
	private final UserService userService;
	private final UserDAO userDAO;
	private final TokenRepository tokenRepository;
	private final AuthenticationManager authenticationManager;

	private AuthResponse generateTokens(User user) {

		log.debug("Generating new JWT tokens.");

		String token = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);

		tokenRepository.saveAll(
				List.of(
						new Token(token, user, TokenType.BEARER, false),
						new Token(refreshToken, user, TokenType.BEARER, false)));

		log.debug("JWT tokens generated successfully.");

		return new AuthResponse(
				token,
				refreshToken);
	}

	private void revokeAllValidUserTokens(User user) {
		log.debug("Revoking %s's tokens.".formatted(user.getEmail()));

		List<Token> validTokens = tokenRepository.findAllByUserIdAndRevoked(user.getId(), false);
		validTokens.forEach(
				token -> token.setRevoked(true));
		tokenRepository.saveAll(validTokens);
		log.debug("All tokens have been revoked.");
	}

	public AuthResponse register(AuthRequest newUser) {
		log.info("Registering a new user.");
		userDAO.getUserByEmail(newUser.getEmail())
				.ifPresent(u -> {
					throw new DuplicateResourceException(
							"%s already exist".formatted(u.getEmail()));
				});

		User user = new User(
				newUser.getEmail(),
				newUser.getPassword(),
				UserType.USER,
				true,
				true,
				true,
				true
		);

		userService.createUser(user);

		log.info("New user registered successfully.");

		AuthResponse res = generateTokens(user);

		return res;
	}

	public AuthResponse authenticate(AuthRequest loginDetails) throws Exception {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginDetails.getEmail(),
						loginDetails.getPassword()));

		User user = userDAO.getUserByEmail(loginDetails.getEmail()).get();

		log.info("New successful login.");

		revokeAllValidUserTokens(user);


		return generateTokens(user);
	}

}
