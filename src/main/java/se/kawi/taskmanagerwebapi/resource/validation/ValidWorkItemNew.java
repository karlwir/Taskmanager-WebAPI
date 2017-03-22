package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerservicelib.model.WorkItem;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidWorkItemNew.Validator.class)

public @interface ValidWorkItemNew {
	
    String message() default "Invalid work item";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidWorkItemNew, WorkItem> {

		@Override
		public void initialize(ValidWorkItemNew constraintAnnotation) {}

		@Override
		public boolean isValid(WorkItem workItem, ConstraintValidatorContext context) {
			return workItem != null &&
				   workItem.getId() != null &&
				   workItem.getTitle() != null &&
				   workItem.getDescription() != null;
		}
    }
}
