package tugba.mining.dto;

import java.io.Serializable;
import java.util.List;

import groovy.transform.builder.Builder;
import lombok.Data;
import tugba.mining.domain.Event;

@Data
@Builder
public class ProcessMap implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Event> events;
	
}
