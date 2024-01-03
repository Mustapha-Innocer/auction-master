package com.auctionmaster.token;

import com.auctionmaster.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity(name = "Token")
@Table(name = "tokens")
@NoArgsConstructor
@RequiredArgsConstructor
public class Token {

	@Id
	@SequenceGenerator(
		name = "token_id_squence",
		sequenceName = "token_id_squence",
		allocationSize = 1
	)
	@GeneratedValue(
		generator = "token_id_squence",
		strategy = GenerationType.SEQUENCE
	)
	@Column(nullable = false, updatable = false)
	private Long id;
	
	@NonNull
	@Column(nullable = false)
	private String token;

	@NonNull
	@ManyToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_fk"), nullable = false)
	private User user;

	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TokenType tokenType;
	
	@NonNull
	@Column(nullable = false)
	private Boolean revoked;

	@Override
	public String toString() {
		return "Token [id=" + id + ", token=" + token + ", userId=" + user.getId() + ", tokenType=" + tokenType + ", revoked="
				+ revoked + "]";
	}
	
}
