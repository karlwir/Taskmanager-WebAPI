package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerservicelib.model.User;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUserNew.Validator.class)
public @interface ValidUserNew {
	
    String message() default "Invalid user";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidUserNew, User> {

		@Override
		public void initialize(ValidUserNew constraintAnnotation) {}

		@Override
		public boolean isValid(User user, ConstraintValidatorContext context) {
			return user != null &&
				   user.getFirstname() != null && 
				   user.getLastname() != null && 
				   user.getUsername() != null;
		}
    }
}
