package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerservicelib.model.Issue;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIssue.Validator.class)

public @interface ValidIssue {
	
    String message() default "Invalid work item";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidIssue, Issue> {

		@Override
		public void initialize(ValidIssue constraintAnnotation) {}

		@Override
		public boolean isValid(Issue issue, ConstraintValidatorContext context) {
			return issue != null &&
				   issue.getId() != null &&
				   issue.getTitle() != null &&
				   issue.getDescription() != null;
		}
    }
}
