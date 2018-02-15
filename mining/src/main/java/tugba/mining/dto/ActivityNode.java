package tugba.mining.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import tugba.mining.domain.Activity;
import tugba.mining.domain.Event;
import tugba.mining.domain.Path;
import tugba.mining.domain.Patient;
@Data
@Builder
public class ActivityNode {
	Activity activity;
	private List<Integer> patients;
	private List<Event> events;
}
