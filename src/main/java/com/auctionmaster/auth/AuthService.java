package com.auctionmaster.auth;

import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auctionmaster.config.JwtService;
import com.auctionmaster.exception.DuplicateResourceException;
import com.auctionmaster.token.Token;
import com.auctionmaster.token.TokenRepository;
import com.auctionmaster.token.TokenType;
import com.auctionmaster.user.User;
import com.auctionmaster.user.UserDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UserDAO userDAO;
	private final TokenRepository tokenRepository;
	private final AuthenticationManager authenticationManager;

	private AuthResponse generateTokens(User user) {

		AuthUser authUser = new AuthUser(
				user.getEmail(),
				user.getPassword(),
				user.getRole().getAuthorities(),
				true,
				true,
				true,
				true);

		String token = jwtService.generateToken(authUser);
		String refreshToken = jwtService.generateRefreshToken(authUser);

		tokenRepository.saveAll(
				List.of(
						new Token(token, user, TokenType.BEARER, false),
						new Token(refreshToken, user, TokenType.BEARER, false)));

		return new AuthResponse(
				token,
				refreshToken);
	}

	private void revokeAllValidUserTokens(User user) {
		List<Token> validTokens = tokenRepository.findAllByUserIdAndRevoked(user.getId(), false);
		validTokens.forEach(
			token -> token.setRevoked(true)
		);
		tokenRepository.saveAll(validTokens);
	}

	public AuthResponse register(User newUser) {

		Optional<User> user = userDAO.getUserByEmail(newUser.getEmail());

		user.ifPresent(u -> {
			throw new DuplicateResourceException(
					"%s already exist".formatted(u.getEmail()));
		});

		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser = userDAO.saveUser(newUser);

		return generateTokens(newUser);
	}

	public AuthResponse authenticate(User loginDetails) throws Exception {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginDetails.getEmail(),
						loginDetails.getPassword()));

		User user = userDAO.getUserByEmail(loginDetails.getEmail()).get();

		revokeAllValidUserTokens(user);

		return generateTokens(user);
	}

}
