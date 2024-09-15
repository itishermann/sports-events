package com.etna.sportevents;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.models.UserRole;
import com.etna.sportevents.payloads.request.RegisterPayload;
import com.etna.sportevents.repositories.UserRepository;
import com.etna.sportevents.services.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Locale;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationControllerTest {
	@Autowired
  private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final Faker faker = new Faker(new Locale("fr"));
	@Test
  public void createManyUsers() {
    for (int i = 0; i < 100; i++) {
	    UserEntity userEntity = new UserEntity();
			userEntity.setUsername(faker.name().username());
			userEntity.setPassword(passwordEncoder.encode("password"));
			userEntity.setEmail(faker.internet().emailAddress());
			userEntity.setRole(faker.options().option(UserRole.class));
	    userRepository.save(userEntity);
    }
  }
}
