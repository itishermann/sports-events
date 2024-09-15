package com.etna.sportevents;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.models.EventEntity;
import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.payloads.response.Event;
import com.etna.sportevents.payloads.response.User;
import com.etna.sportevents.repositories.EventRepository;
import com.etna.sportevents.repositories.RegistrationRepository;
import com.etna.sportevents.repositories.SportRepository;
import com.etna.sportevents.repositories.UserRepository;
import com.etna.sportevents.security.JwtUserDetails;
import com.etna.sportevents.services.EventService;
import com.etna.sportevents.services.RegistrationService;
import com.etna.sportevents.services.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;
import java.util.Objects;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationControllerTest {
	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private RegistrationRepository registrationRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserRepository userRepository;

	private final Faker faker = new Faker(new Locale("fr"));

	@Test
	void createManyRegistrations(){
		Page<User> users = userService.getAllUsers(0, 100);
		Page<Event> events = eventService.getAllEvents(0, 100);
		for (int i = 0; i < 300; i++) {
			// get an random event
			Event event = events.getContent().get(faker.number().numberBetween(0, events.getContent().size()));
			// get a random user that is not the event owner
			User user = users.getContent().get(faker.number().numberBetween(0, users.getContent().size()));
			while (Objects.equals(user.getId(), event.getOrganizer().getId())) {
				user = users.getContent().get(faker.number().numberBetween(0, users.getContent().size()));
			}
			UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow();
			JwtUserDetails userDetails = new JwtUserDetails(userEntity);
			try {
				if(registrationRepository.countByEventId(event.getId()) < event.getMax_participants()){
					registrationService.createRegistration(event.getId(), userDetails);
				}
			} catch (AlreadyExistsException | NotFoundException ignored) {
			}
		}
	}
}
