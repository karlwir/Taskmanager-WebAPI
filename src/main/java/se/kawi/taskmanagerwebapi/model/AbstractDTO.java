package se.kawi.taskmanagerwebapi.model;

import java.net.URI;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import se.kawi.taskmanagerservicelib.model.AbstractEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDTO {

	@JsonProperty
	private String itemKey;
	
	@JsonProperty
	private URI origin;

	@JsonProperty(access = Access.READ_ONLY)
	private LocalDateTime createdDate;

	@JsonProperty(access = Access.READ_ONLY)
	private LocalDateTime updateDate;
	
	protected AbstractDTO() {}
	
	protected AbstractDTO(AbstractEntity abstractEntity) {
		this.itemKey = abstractEntity.getItemKey();
		this.createdDate = abstractEntity.getCreatedDate();
		this.updateDate = abstractEntity.getUpdateDate();
	}
	
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
