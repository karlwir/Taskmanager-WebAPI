package se.kawi.taskmanagerwebapi.resource.query;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;


abstract class BaseQueryBean {

	@QueryParam("page") @DefaultValue("0") private int page;
	@QueryParam("pagesize") @DefaultValue("25") private int pageSize;
	@QueryParam("sort") @DefaultValue("asc") private String sort;
	@QueryParam("sortby") private String sortBy;
	
	protected String[] possibleSortArray = new String[0];
	protected String[] defaultSortArray = new String[]{"id"};
	private String[] requestSortArray;
	
	
	public Pageable buildPageable() {
		if(sortBy != null) {
			requestSortArray = sortBy.toLowerCase().split(",");		
		}

System.out.println(Arrays.toString(possibleSortArray));
		if(requestSortArray != null) {
			
			Set<String> possibleSortSet = new LinkedHashSet<>();
			Set<String> requestSortSet = new LinkedHashSet<>();
			
			for(int i = 0 ; i < possibleSortArray.length; i++) {
				possibleSortSet.add(possibleSortArray[i]);
			}
			for(int i = 0; i < requestSortArray.length; i++) {
				requestSortSet.add(requestSortArray[i]);
			}
			requestSortSet.retainAll(possibleSortSet);
			
			if (requestSortSet.size() > 0) {
				requestSortArray = new String[requestSortSet.size()];
				requestSortSet.toArray(requestSortArray);
			} else {
				requestSortArray = defaultSortArray;
			}
		} else {
			requestSortArray = defaultSortArray;
		}
		
System.out.println(Arrays.toString(requestSortArray));

		if(sort.toLowerCase().equals("desc")) {
			return new PageRequest(page, pageSize, Direction.DESC, requestSortArray);
		}
		return new PageRequest(page, pageSize, Direction.ASC, requestSortArray);
	}
}
