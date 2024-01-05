package com.auctionmaster.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class AuthRequest {

	@Email(message = "Invalid email address")
	@NonNull
	@NotBlank
	private String email;

	@NonNull
	@NotBlank
	private String password;
}
