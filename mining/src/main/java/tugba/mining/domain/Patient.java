package tugba.mining.domain;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Patient extends BaseEntity {

	private String patientClass;
	private Integer age;
	private String sex;

}
