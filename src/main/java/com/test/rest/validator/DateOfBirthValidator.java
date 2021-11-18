package com.test.rest.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateOfBirthValidator implements ConstraintValidator<DateOfBirthConstraint, String> {

	@Override
	public void initialize(DateOfBirthConstraint contactNumber) {
	}

	@Override
	public boolean isValid(String dateOfBirthField, ConstraintValidatorContext cxt) {
		Date dateOfBirth = new Date();
		Date today = new Date();
		if (dateOfBirthField == null || dateOfBirthField.length() < 10){
			return false;
		}
		try {
			dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirthField);
			String todayString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			today = new SimpleDateFormat("yyyy-MM-dd").parse(todayString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long diff = dateOfBirth.getTime() - today.getTime();

		long diffIndays = diff / (1000 * 60 * 60 * 24);
		return diffIndays < 0 && dateOfBirthField.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")
				&& (dateOfBirthField.length() == 10);
	}
}
