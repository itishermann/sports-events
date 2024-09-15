package com.etna.sportevents;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.models.EventEntity;
import com.etna.sportevents.models.RegistrationEntity;
import com.etna.sportevents.payloads.request.CreateCommentPayload;
import com.etna.sportevents.payloads.response.Comment;
import com.etna.sportevents.payloads.response.Registration;
import com.etna.sportevents.repositories.CommentRepository;
import com.etna.sportevents.repositories.EventRepository;
import com.etna.sportevents.repositories.RegistrationRepository;
import com.etna.sportevents.repositories.UserRepository;
import com.etna.sportevents.security.JwtUserDetails;
import com.etna.sportevents.services.CommentService;
import com.etna.sportevents.services.RegistrationService;
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


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerTest {
	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private RegistrationRepository registrationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EventRepository eventRepository;

	private final Faker faker = new Faker(new Locale("fr"));

	@Test
	void createManyComments(){
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
