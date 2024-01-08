package com.auctionmaster.userprofile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.auctionmaster.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "UserProfile")
@Table(name = "user_profiles", uniqueConstraints = {
		@UniqueConstraint(name = "username_unique_constraint", columnNames = "username"),
		@UniqueConstraint(name = "contact_unique_constraint", columnNames = "contact_number")
})
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserProfile {

	@Id
	@SequenceGenerator(name = "user_profile_id_sequence", sequenceName = "user_profile_id_sequence", allocationSize = 1)
	@GeneratedValue(generator = "user_profile_id_sequence", strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false, updatable = false)
	@JsonIgnore
	private Long id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_fk"), nullable = false)
	private User user;

	@NotBlank(message = "username cannot be blank")
	@Column(name = "username", nullable = false)
	private String username;

	@NotBlank
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@NotBlank
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@NotBlank
	@Column(name = "contact_number", nullable = false)
	private String contactNumber;

	@Column(name = "profile_picture_url", nullable = false)
	private String profilePictureUrl;

	@NotNull
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@LastModifiedDate
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public UserProfile(User user, String username, String firstName, String lastName, String contactNumber,
			String profilePictureUrl, LocalDate dateOfBirth) {
		this.user = user;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNumber = contactNumber;
		this.profilePictureUrl = profilePictureUrl;
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return "UserProfile [id=" + id + ", userId=" + user.getId() + ", username=" + username + ", firstName="
				+ firstName
				+ ", lastName=" + lastName + ", contactNumber=" + contactNumber + ", profilePictureUrl="
				+ profilePictureUrl + ", dateOfBirth=" + dateOfBirth + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
}
