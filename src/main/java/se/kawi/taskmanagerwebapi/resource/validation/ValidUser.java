package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerwebapi.model.UserDTO;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUser.Validator.class)

public @interface ValidUser {
	
    String message() default "Invalid user";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidUser, UserDTO> {

		@Override
		public void initialize(ValidUser constraintAnnotation) {}

		@Override
		public boolean isValid(UserDTO userDTO, ConstraintValidatorContext context) {
			return userDTO != null &&
				   userDTO.getItemKey().length() == 36 &&
				   userDTO.getFirstname() != null && 
				   userDTO.getLastname() != null && 
				   userDTO.getUsername() != null &&
				   (userDTO.isActiveUser() || !userDTO.isActiveUser());
		}
		
    }
}
