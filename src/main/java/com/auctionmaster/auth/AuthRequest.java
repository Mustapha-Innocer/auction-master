package com.auctionmaster.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequest {

	@NotBlank
	private String email;

	@NotBlank
	private String password;
}
