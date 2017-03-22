package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerservicelib.model.WorkItem;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidWorkItem.Validator.class)

public @interface ValidWorkItem {
	
    String message() default "Invalid work item";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidWorkItem, WorkItem> {

		@Override
		public void initialize(ValidWorkItem constraintAnnotation) {}

		@Override
		public boolean isValid(WorkItem workItem, ConstraintValidatorContext context) {
			return workItem != null &&
				   workItem.getTitle() != null &&
				   workItem.getDescription() != null;
		}
    }
}
