package se.kawi.taskmanagerwebapi.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import se.kawi.taskmanagerservicelib.model.AbstractEntity;
import se.kawi.taskmanagerservicelib.service.BaseService;
import se.kawi.taskmanagerservicelib.service.ServiceException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import se.kawi.taskmanagerwebapi.model.AbstractDTO;
import se.kawi.taskmanagerwebapi.model.DTOFactory;;

abstract class BaseResource<E extends AbstractEntity, S extends BaseService<E, ?>> {

	protected final S service;
	
	@Autowired
	protected DTOFactory<E> dtoFactory;

	@Context
	protected UriInfo uriInfo;

	protected BaseResource(S service) {
		this.service = service;
	}

	protected Response serviceRequest(ServiceRequest serviceRequest) {
		try {
			return serviceRequest.request();
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e.getWebApplicationException();
		}
	}

	protected Response create(E entity) {
		return serviceRequest(() -> {
			E savedEntity = service.save(entity);
			URI location = uriInfo.getAbsolutePathBuilder().path("{itemKey}").resolveTemplate("itemKey", savedEntity.getItemKey()).build();
			return Response.created(location).build();
		});
	}

	protected Response byItemKey(String itemKey) {
		return serviceRequest(() -> {
			E entity = service.getByItemKey(itemKey);
			if (entity != null) {
				return Response.ok().entity(dtoFactory.buildDTO(entity)).build();
			} else {
				return Response.status(404).build();
			}
		});
	}
	
	protected Response get(Specification<E> spec, Pageable pageable) {
		return serviceRequest(() -> {
			List<E> entities = service.query(spec, pageable);
			return Response.ok().entity(dtoFactory.buildDTO(entities)).build();
		});
	}

	public Response count(Specification<E> spec) {
		return serviceRequest (() -> {
			Long quantity = service.count(spec);
			return Response.ok(quantity).build();
		});		
	}
	
	protected Response delete(AbstractDTO abstractDTO) {
		return serviceRequest(() -> {
			E entity = service.getByItemKey(abstractDTO.getItemKey());
			if (entity != null) {
				service.delete(entity);
				return Response.noContent().build();
			}
			return Response.status(404).build();
		});
	}

}
