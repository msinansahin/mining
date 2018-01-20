package tugba.mining.domain;


import org.neo4j.ogm.annotation.GraphId;

public abstract class BaseEntity {

    //@Id
    //@GeneratedValue
	@GraphId(name = "oid")
	private Long oid;

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}



}
