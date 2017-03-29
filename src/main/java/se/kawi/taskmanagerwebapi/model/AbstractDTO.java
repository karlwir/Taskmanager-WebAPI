package se.kawi.taskmanagerwebapi.model;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractDTO {

	@JsonProperty
	protected String itemKey;
	
	@JsonProperty
	private URI origin;
	
	public String getItemKey() {
		return itemKey;
	}
	
	public URI getOrigin() {
		return origin;
	}
	
	public void setOrigin(URI origin) {
		this.origin = origin;
	}
}
