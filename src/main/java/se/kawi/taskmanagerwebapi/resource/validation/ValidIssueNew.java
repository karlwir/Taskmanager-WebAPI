package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerwebapi.model.IssueDTO;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIssueNew.Validator.class)

public @interface ValidIssueNew {
	
    String message() default "Invalid issue";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidIssueNew, IssueDTO> {

		@Override
		public void initialize(ValidIssueNew constraintAnnotation) {}

		@Override
		public boolean isValid(IssueDTO issueDTO, ConstraintValidatorContext context) {
			return issueDTO != null &&
				   issueDTO.getTitle() != null &&
				   issueDTO.getDescription() != null;
		}
    }
}
