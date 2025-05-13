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
				User user = User.builder()
						.email("admin@fawry.com")
						.password(passwordEncoder.encode("fawry"))
						.role(Role.ADMIN)
						.build();

				userRepository.save(user);
				System.out.println("Default admin user created.");
			}
		};
	}
}

/*
user token : eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFobWVkcmFzbG5AdC5jb20iLCJpc3MiOiJyZXNvLmNvbSIsImlhdCI6MTc0NzE3MjIyMCwiZXhwIjoxNzQ3MjU4NjIwfQ.48lTHNp4S6KEZa37KdOnNsWSYNO6f0qg7e5lrl2kF9s

admin token :


 */