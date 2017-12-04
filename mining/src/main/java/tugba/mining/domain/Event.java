package tugba.mining.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Event extends BaseEntity {

	private String doctor;
	
	@ManyToOne
	private Patient patient;
	
	@ManyToOne
	private Activity activity;
}
