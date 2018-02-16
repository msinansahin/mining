package tugba.mining.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import tugba.mining.domain.Activity;
import tugba.mining.domain.Event;
import tugba.mining.domain.Path;
import tugba.mining.domain.Pattern;

@Data
@Builder
public class ProcessMap implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private List<Activity> activities;
	private List<Path> paths;
	private List<Event> events;
	private List<Pattern> patterns;

}
