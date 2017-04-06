package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerwebapi.model.WorkItemDTO;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidWorkItemNew.Validator.class)

public @interface ValidWorkItemNew {
	
    String message() default "Invalid work item";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidWorkItemNew, WorkItemDTO> {

		@Override
		public void initialize(ValidWorkItemNew constraintAnnotation) {}

		@Override
		public boolean isValid(WorkItemDTO workItemDTO, ConstraintValidatorContext context) {
			return workItemDTO != null &&
				   workItemDTO.getTitle() != null &&
				   workItemDTO.getDescription() != null && 
				   workItemDTO.getPriority() != null;
		}
    }
}
