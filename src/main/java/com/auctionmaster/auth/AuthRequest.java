package com.auctionmaster.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class AuthRequest {

	@NonNull
	@NotBlank
	private String email;

	@NonNull
	@NotBlank
	private String password;
}
