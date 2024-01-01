package com.auctionmaster.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

	@JsonProperty("access_token")
	@NonNull
	private String accessToken;

	@JsonProperty("refresh_token")
	@NonNull
	private String refreshToken;
}
