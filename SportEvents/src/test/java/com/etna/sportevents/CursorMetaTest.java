package com.etna.sportevents;

import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.payloads.response.CursorMeta;
import com.etna.sportevents.payloads.response.User;
import com.etna.sportevents.repositories.UserRepository;
import com.etna.sportevents.services.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CursorMetaTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("Correctly creates CursorMeta when there are next and previous pages")
	void correctlyCreatesCursorMetaWhenThereAreNextAndPreviousPages() {
		final int size = 10;
		final int cursor = 20;
		Page<User> page = userService.getAllUsers(cursor, size);
		assertNotNull(page);
		CursorMeta cursorMeta = new CursorMeta(page, cursor, size);
		assertNotNull(cursorMeta);
		assertEquals(String.valueOf(cursor), cursorMeta.getCursor());
		assertEquals(String.valueOf(cursor-size), cursorMeta.getPrevious());
		assertEquals(String.valueOf(cursor+size), cursorMeta.getNext());
	}

	@Test
	@DisplayName("Correctly creates CursorMeta when there are next but no previous pages")
	void correctlyCreatesCursorMetaWhenThereAreNextButNoPreviousPages() {
		final int size = 10;
		final int cursor = 0;
		Page<User> page = userService.getAllUsers(cursor, size);
		assertNotNull(page);
		CursorMeta cursorMeta = new CursorMeta(page, cursor, size);
		assertNotNull(cursorMeta);
		assertEquals(String.valueOf(cursor), cursorMeta.getCursor());
		assertNull(cursorMeta.getPrevious());
		assertEquals(String.valueOf(cursor+size), cursorMeta.getNext());
	}

	@Test
	@DisplayName("Correctly creates CursorMeta when there are no next but previous pages")
	void correctlyCreatesCursorMetaWhenThereAreNoNextButPreviousPages() {
		final int size = 10;
		final int cursor = (int) userRepository.count()-1;
		Page<User> page = userService.getAllUsers(cursor, size);
		assertNotNull(page);
		CursorMeta cursorMeta = new CursorMeta(page, cursor, size);
		assertNotNull(cursorMeta);
		assertEquals(String.valueOf(cursor), cursorMeta.getCursor());
		assertNull(cursorMeta.getNext());
	}

	@Test
	@DisplayName("Correctly creates CursorMeta when there are no next and no previous pages")
	void correctlyCreatesCursorMetaWhenThereAreNoNextAndNoPreviousPages() {
		final int size = 150;
		final int cursor = 0;
		Page<User> page = userService.getAllUsers(cursor, size);
		assertNotNull(page);
		CursorMeta cursorMeta = new CursorMeta(page, cursor, size);
		assertNotNull(cursorMeta);
		assertEquals(String.valueOf(cursor), cursorMeta.getCursor());
		assertNull(cursorMeta.getPrevious());
		assertNull(cursorMeta.getNext());
	}
}
