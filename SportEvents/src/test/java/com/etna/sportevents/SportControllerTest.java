package com.etna.sportevents;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.payloads.request.CreateSportPayload;
import com.etna.sportevents.services.SportService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SportControllerTest {
	@Autowired
	private SportService sportService;

	private final Faker faker = new Faker(new Locale("fr"));
	@Test
	void createManySports() throws AlreadyExistsException {
		for (int i = 0; i < 100; i++) {
			CreateSportPayload createSportPayload = new CreateSportPayload(faker.funnyName().name());
			try {
				sportService.createSport(createSportPayload);
			} catch (AlreadyExistsException ignored) {
			}
		}
	}
}
