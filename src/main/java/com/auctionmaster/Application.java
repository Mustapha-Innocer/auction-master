package com.auctionmaster;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.auctionmaster.user.User;
import com.auctionmaster.user.UserService;
import com.auctionmaster.user.UserType;
import com.auctionmaster.auth.AuthRequest;
import com.auctionmaster.auth.AuthService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(title = "Auction Master", description = "Rest API Documentation for Auction master", version = "1.0"), security = {
		@SecurityRequirement(name = "jwtAuth")
})
@SecurityScheme(name = "jwtAuth", description = "Jwt authentication scheme", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@EnableJpaAuditing
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner runner(
			UserService userService,
			AuthService authService) {
		return args -> {
			// User
			AuthRequest user = new AuthRequest("user@gmail.com", "12345");
			authService.register(user);

			User admin = new User(
					"admin@gmail.com",
					"12345",
					UserType.ADMIN,
					true,
					true,
					true,
					true);
			userService.createUser(admin);
		};
	}

}
