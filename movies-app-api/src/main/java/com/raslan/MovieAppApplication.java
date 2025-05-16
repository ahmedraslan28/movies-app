package com.raslan;

import com.raslan.model.Role;
import com.raslan.model.User;
import com.raslan.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MovieAppApplication {


	public static void main(String[] args) {
		SpringApplication.run(MovieAppApplication.class, args);
	}
	@Bean
	public CommandLineRunner addDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (!userRepository.existsByEmail("admin@fawry.com")) {
				User admin = User.builder()
						.email("admin@fawry.com")
						.password(passwordEncoder.encode("fawry"))
						.role(Role.ADMIN)
						.build();

				User user = User.builder()
						.email("user@fawry.com")
						.password(passwordEncoder.encode("fawry"))
						.role(Role.USER)
						.build();

				userRepository.save(user);
				userRepository.save(admin);
				System.out.println("Default users created.");
			}
		};
	}
}