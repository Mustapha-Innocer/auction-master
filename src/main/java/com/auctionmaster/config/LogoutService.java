package com.auctionmaster.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.auctionmaster.token.Token;
import com.auctionmaster.token.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler{
	
	private final TokenRepository tokenRepository;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}

		final String jwt = authHeader.replace("Bearer ", "");

		Token token = tokenRepository.findByToken(jwt).orElse(null);

		if (token != null) {
			token.setRevoked(true);
			tokenRepository.save(token);
			SecurityContextHolder.clearContext();
		}
	}
	
}
