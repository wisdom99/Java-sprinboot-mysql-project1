package com.test.rest.controller;

import com.test.rest.model.User;
import com.test.rest.service.UserService;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/save")
	public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
		userService.saveUser(user);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/get")
	public ResponseEntity<String> getUser(@RequestParam String username) {
		String retrievedMessaage = userService.getUser(username);
		return new ResponseEntity<>(retrievedMessaage, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
			log.info("Validation error field: {} - value: {}", fieldName, errorMessage);
		});
//		errors.forEach((key, value) -> log.info("Validation error field: {} - value: {}", key, value));
		return errors;
	}
}
