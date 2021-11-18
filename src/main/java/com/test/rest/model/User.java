package com.test.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.rest.validator.DateOfBirthConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@Pattern(regexp = "^[a-zA-Z_ ]*$", message = "User name must be letters")
	@NotBlank(message = "User name is required")
	private String  userName;

	@Column(nullable = false)
//	@NotBlank(message = "Date of birth is required")
	@DateOfBirthConstraint(message = "Invalid date of birth, please use format yyyy-MM-dd, before today")
	private String dateOfBirth;

}

