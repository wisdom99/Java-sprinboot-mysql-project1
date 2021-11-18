package com.test.rest.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = DateOfBirthValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateOfBirthConstraint{

	String message() default "Date of birth";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
