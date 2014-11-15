package no.uninett.adc.neo.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class SystemURN {

	@GraphId
	private Long id;
	@Indexed(unique = true)
	private String urn;

	@Deprecated
	public SystemURN() {
	}

	public SystemURN(final String urn) {
		this.urn = urn;
	}

	public String getUrn() {
		return urn;
	}


	public void setUrn(final String urn) {
		this.urn = urn;
	}

}
