package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerwebapi.model.TeamDTO;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTeamNew.Validator.class)
public @interface ValidTeamNew {
	
    String message() default "Invalid team";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidTeamNew, TeamDTO> {

		@Override
		public void initialize(ValidTeamNew constraintAnnotation) {}

		@Override
		public boolean isValid(TeamDTO teamDTO, ConstraintValidatorContext context) {
			return teamDTO != null &&
				   teamDTO.getTeamName() != null;
		}
    }
}
