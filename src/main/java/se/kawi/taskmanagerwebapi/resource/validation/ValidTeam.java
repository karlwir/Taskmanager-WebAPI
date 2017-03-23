package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerwebapi.model.TeamDTO;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTeam.Validator.class)
public @interface ValidTeam {
	
    String message() default "Invalid team";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidTeam, TeamDTO> {

		@Override
		public void initialize(ValidTeam constraintAnnotation) {}

		@Override
		public boolean isValid(TeamDTO teamDTO, ConstraintValidatorContext context) {
			return teamDTO != null &&
				   teamDTO.getItemKey() != null && 
				   teamDTO.getItemKey().length() == 36 &&
				   teamDTO.getItemKey().substring(0, 4).equals("b2db") &&
				   teamDTO.getTeamName() != null;
		}
    }
}
