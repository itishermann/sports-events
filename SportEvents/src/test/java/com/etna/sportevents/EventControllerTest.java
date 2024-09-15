package com.etna.sportevents;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.models.SportEntity;
import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.models.UserRole;
import com.etna.sportevents.payloads.request.CreateEventPayload;
import com.etna.sportevents.payloads.request.CreateSportPayload;
import com.etna.sportevents.payloads.request.RegisterPayload;
import com.etna.sportevents.payloads.response.Sport;
import com.etna.sportevents.repositories.SportRepository;
import com.etna.sportevents.repositories.UserRepository;
import com.etna.sportevents.services.EventService;
import com.etna.sportevents.services.SportService;
import com.github.javafaker.Faker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventControllerTest {
	@Autowired
  private MockMvc mockMvc;

	@Autowired
	private EventService eventService;

	@Autowired
	private SportRepository sportRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SportService sportService;

	private final Faker faker = new Faker(new Locale("fr"));
	@Test
  public void createManyEvents() throws AlreadyExistsException, ParseException, JSONException {
		Page<UserEntity> users = userRepository.findAll(PageRequest.of(0, 100));
		Page<SportEntity> sports = sportRepository.findAll(PageRequest.of(0, 100));
		JSONArray locations = new MockLocations().getLocations();
    for (int i = 0; i < 100; i++) {
			// get random sport
			SportEntity sport = sports.getContent().get(faker.number().numberBetween(0, 100));
			// get random user
			UserEntity user = users.getContent().get(faker.number().numberBetween(0, 100));
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
}
