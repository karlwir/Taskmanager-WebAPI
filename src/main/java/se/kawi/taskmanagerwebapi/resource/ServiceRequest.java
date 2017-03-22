package se.kawi.taskmanagerwebapi.resource;

import javax.ws.rs.core.Response;

import se.kawi.taskmanagerservicelib.service.ServiceException;

interface ServiceRequest {
	Response request() throws ServiceException;
}
