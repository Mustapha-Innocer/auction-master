package com.auctionmaster.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long>{
	
	Optional<Token> findByToken(String token);

	List<Token> findAllByUserIdAndRevoked(Long userId, Boolean revoked);
}
