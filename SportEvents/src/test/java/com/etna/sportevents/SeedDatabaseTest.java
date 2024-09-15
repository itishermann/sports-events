package com.etna.sportevents;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.models.*;
import com.etna.sportevents.payloads.request.CreateCommentPayload;
import com.etna.sportevents.payloads.request.CreateEventPayload;
import com.etna.sportevents.payloads.request.CreateSportPayload;
import com.etna.sportevents.payloads.response.Comment;
import com.etna.sportevents.payloads.response.Event;
import com.etna.sportevents.payloads.response.User;
import com.etna.sportevents.repositories.*;
import com.etna.sportevents.security.JwtUserDetails;
import com.etna.sportevents.services.*;
import com.github.javafaker.Faker;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeedDatabaseTest {
	@Autowired
  private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SportService sportService;

	@Autowired
	private EventService eventService;

	@Autowired
	private SportRepository sportRepository;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private RegistrationRepository registrationRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	private EventRepository eventRepository;

	@Test
	@Order(1)
	void createManySports() throws AlreadyExistsException {
		final Faker faker = new Faker(new Locale("fr"));
		for (int i = 0; i < 100; i++) {
			CreateSportPayload createSportPayload = new CreateSportPayload(faker.funnyName().name());
			try {
				sportService.createSport(createSportPayload);
			} catch (AlreadyExistsException ignored) {
			}
		}
	}

	@Test
	@Order(2)
  public void createManyUsers() {
		final Faker faker = new Faker(new Locale("fr"));
    for (int i = 0; i < 100; i++) {
	    UserEntity userEntity = new UserEntity();
			userEntity.setUsername(faker.name().username());
			userEntity.setPassword(passwordEncoder.encode("password"));
			userEntity.setEmail(faker.internet().emailAddress());
			userEntity.setRole(faker.options().option(UserRole.class));
			try {
				userRepository.save(userEntity);
			} catch (Exception ignored) {
			}
    }
  }

	@Test
	@Order(3)
  public void createManyEvents() throws AlreadyExistsException, ParseException, JSONException {
		final Faker faker = new Faker(new Locale("fr"));
		Page<UserEntity> users = userRepository.findAll(PageRequest.of(0, 100));
		Page<SportEntity> sports = sportRepository.findAll(PageRequest.of(0, 100));
		JSONArray locations = new MockLocations().getLocations();
    for (int i = 0; i < 100; i++) {
			// get random sport
			SportEntity sport = sports.getContent().get(faker.number().numberBetween(0, sports.getNumberOfElements()));
			// get random user
			UserEntity user = users.getContent().get(faker.number().numberBetween(0, users.getNumberOfElements()));
	    CreateEventPayload payload = new CreateEventPayload(
					faker.funnyName().name(),
			    faker.address().fullAddress(),
					faker.date().past(14, TimeUnit.DAYS),
			    faker.date().future(14, TimeUnit.DAYS),
			    faker.number().numberBetween(1, 10),
			    Double.parseDouble(
						locations.getJSONArray(i).get(0).toString()
			    ),
			    Double.parseDouble(
						locations.getJSONArray(i).get(1).toString()
			    ),
			    sport.getId()
	    );
			try {
				eventService.createEvent(user.getId(), payload);
			} catch (NotFoundException e) {
				throw new RuntimeException(e);
			}
    }
  }

	@Test
	@Order(4)
	void createManyRegistrations(){
		final Faker faker = new Faker(new Locale("fr"));
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

	@Test
	@Order(5)
	void createManyComments(){
		final Faker faker = new Faker(new Locale("fr"));
		Page<EventEntity> events = eventRepository.findAll(PageRequest.of(0, 100));
		for(int i = 0; i < 10000; i++){
			EventEntity event = events.getContent().get(faker.number().numberBetween(0, events.getContent().size()));
			CreateCommentPayload payload = new CreateCommentPayload(
					faker.lorem().characters(10,250),
					(float) faker.number().numberBetween(1, 5)
			);
			// pick a random registration from the event
			Page<RegistrationEntity> registrations = registrationRepository.findAllByIdGreaterThanAndEventIdIs(0,event.getId(), PageRequest.of(0, 100));
			if(registrations.getTotalElements() == 0){
				continue;
			}
			RegistrationEntity registration = registrations.getContent().get((int) faker.number().numberBetween(0, registrations.getTotalElements()));
			JwtUserDetails jwtUserDetails = new JwtUserDetails(registration.getUser());
			try {
				Comment c = commentService.createComment(
						event.getId(),
						jwtUserDetails,
						payload
				);
				// pick a random bool
				if(faker.bool().bool()){
					commentService.approveById(c.getId());
				}
			} catch (Exception | AlreadyExistsException | NotFoundException ignored) {
			}
		}

	}
}
