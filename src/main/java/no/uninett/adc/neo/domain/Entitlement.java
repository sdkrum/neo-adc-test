package no.uninett.adc.neo.domain;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Entitlement {

	@GraphId
	private Long id;
	@Indexed(unique = true)
	private String entitlement;
	@RelatedTo(type = "ENTITLED", direction = Direction.INCOMING)
	private Set<EduPerson> persons;

	public Entitlement() {
	}

	public Entitlement(String string) {
		this.entitlement = string;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((entitlement == null) ? 0 : entitlement.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Entitlement)) {
			return false;
		}
		Entitlement other = (Entitlement) obj;
		if (entitlement == null) {
			if (other.entitlement != null) {
				return false;
			}
		} else if (!entitlement.equals(other.entitlement)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the entitlement
	 */
	public String getEntitlement() {
		return entitlement;
	}

	/**
	 * @param entitlement
	 *            the entitlement to set
	 */
	public void setEntitlement(String entitlement) {
		this.entitlement = entitlement;
	}

	/**
	 * @return the persons
	 */
	public Set<EduPerson> getPersons() {
		return persons;
	}
	
}
