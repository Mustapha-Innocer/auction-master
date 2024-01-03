package com.auctionmaster.config;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${application.security.jwt.secret-key}")
	private String secretKey;

	@Value("${application.security.jwt.expirations.token}")
	private long tokenExpiration;

	@Value("${application.security.jwt.expirations.refresh-token}")
	private long refreshTokenExpiration;

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private String buildToken(
			UserDetails userDetails,
			long expiration) {
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignKey())
				.compact();
	}

	public String generateToken(UserDetails userDetails) {
		return buildToken(userDetails, tokenExpiration);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return buildToken(userDetails, refreshTokenExpiration);
	}

	private Claims extractClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractClaims(token);
		return claimsResolver.apply(claims);
	}

	public String extractUsername(String jwt) {
		return extractClaim(jwt, Claims::getSubject);

	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
}
