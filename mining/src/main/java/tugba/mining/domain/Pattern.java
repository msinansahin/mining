package tugba.mining.domain;


import org.neo4j.ogm.annotation.NodeEntity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tugba.mining.domain.Patient.PatientBuilder;

//@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Pattern extends BaseEntity {
	private Integer patternId;
	private String trace;
	private Integer eventNumber;
	private String myPatients;

}
