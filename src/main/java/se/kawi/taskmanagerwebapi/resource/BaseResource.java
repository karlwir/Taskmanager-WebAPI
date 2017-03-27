package se.kawi.taskmanagerwebapi.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import se.kawi.taskmanagerservicelib.model.AbstractEntity;
import se.kawi.taskmanagerservicelib.service.BaseService;
import se.kawi.taskmanagerservicelib.service.ServiceDataFormatException;
import se.kawi.taskmanagerservicelib.service.ServiceDataSourceException;
import se.kawi.taskmanagerservicelib.service.ServiceEntityNotFoundException;
import se.kawi.taskmanagerservicelib.service.ServiceException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import se.kawi.taskmanagerwebapi.exception.BadRequestException;
import se.kawi.taskmanagerwebapi.exception.NotFoundException;
import se.kawi.taskmanagerwebapi.model.AbstractDTO;
import se.kawi.taskmanagerwebapi.model.DTOFactory;

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
		} catch (ServiceDataFormatException e) {
			throw new BadRequestException(e.getMessage());
		} catch (ServiceEntityNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (ServiceDataSourceException e) {
			throw new WebApplicationException(500);
		} catch (ServiceException e) {
			throw new WebApplicationException(500);
		}
	}

	protected Response create(E entity) {
		return serviceRequest(() -> {
			E savedEntity = service.save(entity);
			URI location = uriInfo.getAbsolutePathBuilder().path(savedEntity.getItemKey()).build();
			return Response.created(location).build();
		});
	}

	protected Response byItemKey(String itemKey) {
		return serviceRequest(() -> {
			E entity = service.getByItemKey(itemKey);
			return Response.ok().entity(dtoFactory.buildDTO(entity)).build();
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
			service.delete(entity);
			return Response.noContent().build();
		});
	}

}
