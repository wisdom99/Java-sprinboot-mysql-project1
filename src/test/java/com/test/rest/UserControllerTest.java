package com.test.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.rest.model.User;
import com.test.rest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	User user = new User();
	String userName;

	String dateOfBirth;


	@BeforeEach
	public void beforeEach() {

		userRepository.deleteAll();

		userName = "testuser";
		dateOfBirth = "1998-01-25";

		this.user.setUserName(userName);
		this.user.setDateOfBirth(dateOfBirth);
	}

	@Test
	public void whenPostRequestToUsersAndValidUser_thenCorrectResponse() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
				.content(objectMapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}

	@Test
	public void whenPostRequestToUsersAndInValidUser_thenFailureResponse() throws Exception {

		user = new User();
		user.setDateOfBirth("");
		user.setUserName("");
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
				.content(objectMapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

		String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
		assertThat(contentAsString).contains("\"userName\":\"User name is required\"");

	}

	@Test
	public void whenPostRequestToUsersAndInValidDateOfBirth_thenFailureResponse() throws Exception {

		user = new User();
		user.setDateOfBirth("1998-13-13");
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
				.content(objectMapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

		String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
		assertThat(contentAsString).contains("\"dateOfBirth\":\"Invalid date of birth, please use format yyyy-MM-dd, before today\"");
	}

	@Test
	public void whenPostRequestToUsersAndInValidUserName_thenFailureResponse() throws Exception {

		user.setUserName("chubueze1");
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
				.content(objectMapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

		String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
		assertThat(contentAsString).contains("\"userName\":\"User name must be letters\"");
	}

	@Test
	public void whenPostRequestToUsersAndValidDateOfBirth_thenSuccessResponse() throws Exception {

		LocalDateTime localDate = LocalDateTime.now().minusYears(10);
		Date todayMinusYear = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
		String today = new SimpleDateFormat("yyyy-MM-dd").format(todayMinusYear);


		user.setDateOfBirth(today);
		mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
				.content(objectMapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
		requestParam.add("username", userName);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/user/get")
				.params(requestParam)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

		String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
		assertThat(contentAsString).contains("Happy birthday");
	}

	@Test
	public void whenPostRequestToUsersAndValidUserName_thenSuccessResponse() throws Exception {

		LocalDateTime localDate = LocalDateTime.now().minusDays(10);
		Date todayMinusDay = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
		String today = new SimpleDateFormat("yyyy-MM-dd").format(todayMinusDay);
		System.out.println("\n\n\n\n\nn\\nn\n\nn\n\n\n\n\nn\n " + today);
		user.setDateOfBirth(today);

		mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
				.content(objectMapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
		requestParam.add("username", userName);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/user/get")
				.params(requestParam)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

		String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
		assertThat(contentAsString).contains("Your birthday is in " + 10 + " day(s) time");
	}
}
