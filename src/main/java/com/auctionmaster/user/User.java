package com.auctionmaster.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.auctionmaster.token.Token;
import com.auctionmaster.userprofile.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "User")
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(name = "email_unique_constraint", columnNames = "email")
})

@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails{

	@Id
	@SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password_hash", nullable = false)
	private String password;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType role;

	@JsonIgnore
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private UserProfile profile;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Token> tokens;

	@JsonIgnore
	@Transient
	private Collection<? extends GrantedAuthority> authorities;
	
	@JsonIgnore
	@Transient
	private String username;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "non_expired", nullable = false)
	private boolean accountNonExpired;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "non_locked", nullable = false)
	private boolean accountNonLocked;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "credentials_non_expired", nullable = false)
	private boolean credentialsNonExpired;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updatedAt;

	public User(String email, String password, UserType role) {
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(String email, String password, UserType role, boolean accountNonExpired, boolean accountNonLocked,
			boolean credentialsNonExpired, boolean enabled) {
		this.email = email;
		this.password = password;
		this.role = role;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role.getAuthorities();
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
