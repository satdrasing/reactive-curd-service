package com.satendra.employeeservice;

import com.satendra.employeeservice.daos.UserRepository;
import com.satendra.employeeservice.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.mongo.config.annotation.web.reactive.EnableMongoWebSession;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.HeaderWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceApplication {

	final private UserRepository users;
	final private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(){
		return args -> {
			this.users
					.deleteAll()
					.thenMany(
							Flux
									.just("user", "admin")
									.flatMap(
											username -> {
												List<String> roles = "user".equals(username)
														? Arrays.asList("ROLE_USER")
														: Arrays.asList("ROLE_USER", "ROLE_ADMIN");

												User user = User.builder()
														.roles(roles)
														.username(username)
														.password(passwordEncoder.encode("password"))
														.email(username + "@example.com")
														.build();
												return this.users.save(user);
											}
									)
					)
					.log()
					.subscribe(
							null,
							null,
							() -> log.info("done users initialization...")
					);
		};
	}
}

@Configuration
@EnableMongoWebSession
class SessionConfig {

	@Bean
	public WebSessionIdResolver webSessionIdResolver() {
		//var resolver = new CookieWebSessionIdResolver();
		var resolver = new HeaderWebSessionIdResolver();
		resolver.setHeaderName("X-AUTH-TOKEN");
		return resolver;
	}
}
