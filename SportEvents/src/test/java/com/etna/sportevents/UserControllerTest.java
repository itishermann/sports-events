package com.etna.sportevents;

import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.models.UserRole;
import com.etna.sportevents.repositories.UserRepository;
import com.github.javafaker.Faker;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Locale;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
	@Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

	private static final HashMap<String,String> normalUser = new HashMap<String,String>(){{
        put("username", "test1");
        put("password", "password");
        put("email", "test1@test1.com");
    }};

    private static final HashMap<String,String> adminUser = new HashMap<String,String>(){{
        put("username", "test2");
        put("password", "password");
        put("email", "test2@test2.com");
    }};


	private int userId;
  private int adminId;

  private String userToken;
  private String adminToken;

  private final Faker faker = new Faker(new Locale("fr"));

	@BeforeAll
    public void setup() throws Exception {
        UserEntity user1 = userRepository.findByUsername(normalUser.get("username"));
        UserEntity user2 = userRepository.findByUsername(adminUser.get("username"));
        if (user1 != null) {
            userRepository.delete(user1);
        }
        if (user2 != null) {
            userRepository.delete(user2);
        }
        this.mockMvc
                .perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(new JSONObject(normalUser).toString())
                                .accept("application/json")
                )
                .andDo(print())
                .andExpect(status().isCreated());
        String response = this.mockMvc
                .perform(
                        post("/api/auth/login")
                                .contentType("application/json")
                                .content(new JSONObject(normalUser).toString())
                                .accept("application/json")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(response);
        JSONObject jsonResponse = new JSONObject(response);
        assertTrue(jsonResponse.has("data"));
        assertTrue(jsonResponse.getJSONObject("data").has("accessToken"));
        userToken = jsonResponse.getJSONObject("data").getString("accessToken");
        user1 = userRepository.findByUsername(normalUser.get("username"));
        userId = user1.getId();

        this.mockMvc
                .perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(new JSONObject(adminUser).toString())
                                .accept("application/json")
                )
                .andDo(print())
                .andExpect(status().isCreated());
        response = this.mockMvc
                .perform(
                        post("/api/auth/login")
                                .contentType("application/json")
                                .content(new JSONObject(adminUser).toString())
                                .accept("application/json")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(response);
        jsonResponse = new JSONObject(response);
         assertTrue(jsonResponse.has("data"));
        assertTrue(jsonResponse.getJSONObject("data").has("accessToken"));
        adminToken = jsonResponse.getJSONObject("data").getString("accessToken");
        user2 = userRepository.findByUsername(adminUser.get("username"));
        user2.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(user2);
        adminId = user2.getId();
    }

		@AfterAll
    public void cleanup() {
        UserEntity user1 = userRepository.findByUsername(normalUser.get("username"));
        UserEntity user2 = userRepository.findByUsername(adminUser.get("username"));
        if (user1 != null) {
            userRepository.delete(user1);
        }
        if (user2 != null) {
            userRepository.delete(user2);
        }
    }



	@Test
  @Order(5)
  @DisplayName("La route PUT /user/:id répond bien en 200 et retourne bien le user à jour.")
  void updateUserByIdShouldReturn200() throws Exception {
      HashMap<String,String> updatedUser = new HashMap<>();
      updatedUser.put("username", faker.name().username());
      String response = this.mockMvc
              .perform(
                      put("/api/user/" + userId)
                              .contentType(MediaType.APPLICATION_JSON)
                              .accept(MediaType.APPLICATION_JSON)
	                            .header("Authorization", "Bearer " + userToken)
                              .content(new JSONObject(updatedUser).toString())
              )
              .andDo(print())
              .andExpect(status().isOk())
              .andReturn().getResponse().getContentAsString();
      assertNotNull(response);
      JSONObject jsonResponse = new JSONObject(response);
      assertTrue(jsonResponse.has("data"));
      assertEquals(updatedUser.get("username"), jsonResponse.getJSONObject("data").getString("username"));
  }
}
