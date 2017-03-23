package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerservicelib.model.Issue;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIssueNew.Validator.class)

public @interface ValidIssueNew {
	
    String message() default "Invalid issue";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidIssueNew, Issue> {

		@Override
		public void initialize(ValidIssueNew constraintAnnotation) {}

		@Override
		public boolean isValid(Issue issue, ConstraintValidatorContext context) {
			return issue != null &&
				   issue.getTitle() != null &&
				   issue.getDescription() != null;
		}
    }
}
