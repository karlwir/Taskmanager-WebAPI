package se.kawi.taskmanagerwebapi.resource.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import se.kawi.taskmanagerwebapi.model.WorkItemDTO;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidWorkItem.Validator.class)

public @interface ValidWorkItem {
	
    String message() default "Invalid work item";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
	public class Validator implements ConstraintValidator<ValidWorkItem, WorkItemDTO> {

		@Override
		public void initialize(ValidWorkItem constraintAnnotation) {}

		@Override
		public boolean isValid(WorkItemDTO workItemDTO, ConstraintValidatorContext context) {
			return workItemDTO != null &&
				   workItemDTO.getItemKey() != null &&
				   workItemDTO.getItemKey().length() == 36 &&
				   workItemDTO.getItemKey().substring(0, 4).equals("b2dd") &&
				   workItemDTO.getTitle() != null &&
				   workItemDTO.getDescription() != null &&
				   workItemDTO.getStatus() != null;
		}
    }
}
