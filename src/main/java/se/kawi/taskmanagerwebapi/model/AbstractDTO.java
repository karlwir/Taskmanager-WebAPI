package se.kawi.taskmanagerwebapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractDTO {
	
	@JsonProperty
	protected String itemKey;
	
	public String getItemKey() {
		return itemKey;
	}
}
