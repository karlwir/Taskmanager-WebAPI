package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerwebapi.model.IssueDTO;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIssue.Validator.class)

public @interface ValidIssue {
	
    String message() default "Invalid issue";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidIssue, IssueDTO> {

		@Override
		public void initialize(ValidIssue constraintAnnotation) {}

		@Override
		public boolean isValid(IssueDTO issueDTO, ConstraintValidatorContext context) {
			return issueDTO != null &&
				   issueDTO.getItemKey() != null &&
				   issueDTO.getItemKey().length() == 36 &&
				   issueDTO.getItemKey().substring(0, 4).equals("b2da") &&
				   issueDTO.getTitle() != null &&
				   issueDTO.getDescription() != null &&
				   issueDTO.isOpen() != null;
		}
    }
}
