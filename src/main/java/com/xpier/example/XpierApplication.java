package com.xpier.example;

import com.xpier.example.model.entities.User;
import com.xpier.example.reposiroty.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@SpringBootApplication
@RequiredArgsConstructor
public class XpierApplication implements CommandLineRunner {

	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(XpierApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*Save current user demo*/
		var existUser = userRepository.existsById(1L);
		if (!existUser)
			userRepository.save(new User(1L, "chiennx", "Nguyen Xuan Chien", Date.from(Instant.now())));
	}
}
