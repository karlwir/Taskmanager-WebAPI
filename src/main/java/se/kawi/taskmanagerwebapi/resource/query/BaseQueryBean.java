package se.kawi.taskmanagerwebapi.resource.query;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;


abstract class BaseQueryBean {

	@QueryParam("size") @DefaultValue("25") private int size;
	@QueryParam("page") @DefaultValue("0") private int page;
	@QueryParam("sort") @DefaultValue("asc") private String sort;
	@QueryParam("sortby") @DefaultValue("id") private String sortBy;
	
	public Pageable buildPageable() {

		String[] sortArray = new String[] {sortBy};
		
		if(!sortBy.equals("id")) {
			Set<String> possibleSortSet = new LinkedHashSet<>();
			Set<String> requestedSortSet = new LinkedHashSet<>();
			
			Field[] possibleSortArray = this.getClass().getDeclaredFields();
			String[] requestedSortArray = sortBy.toLowerCase().split(",");
			
			for(int i = 0 ; i < possibleSortArray.length; i++) {
				possibleSortSet.add(possibleSortArray[i].getName());
			}

			for(int i = 0; i < requestedSortArray.length; i++) {
				requestedSortSet.add(requestedSortArray[i]);
			}
			requestedSortSet.add("id");
			possibleSortSet.add("id");
			
			requestedSortSet.retainAll(possibleSortSet);
			sortArray = new String[requestedSortSet.size()];
			requestedSortSet.toArray(sortArray);
		}
		
		if(sort.toLowerCase().equals("desc")) {
			return new PageRequest(page, size, Direction.DESC, sortArray);
		}
		return new PageRequest(page, size, Direction.ASC, sortArray);
	}
}
