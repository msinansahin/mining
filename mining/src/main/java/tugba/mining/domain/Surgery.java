package tugba.mining.domain;

import java.util.Date;

import org.neo4j.ogm.annotation.NodeEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

//@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Surgery extends BaseEntity{
	
	private int surgeryNo;
	private String surgeryName;
	private String surgeryCategory;

}
