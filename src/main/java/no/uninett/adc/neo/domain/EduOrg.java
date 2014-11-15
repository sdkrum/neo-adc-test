package no.uninett.adc.neo.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class EduOrg extends EduObject {

	@Indexed(unique = true)
	private String orgNIN;
	@RelatedTo(type = RelationshipType.WORKS_FOR_ORG, direction = Direction.INCOMING)
	private Set<EduPerson> employees = new HashSet<EduPerson>();

	public void addEmployee(final EduPerson emp) {
		employees.add(emp);
	}

	public void setOrgNIN(final String orgNIN) {
		this.orgNIN = orgNIN;
	}

	/**
	 * @return the employees
	 */
	public Set<EduPerson> getEmployees() {
		return employees;
	}

	/**
	 * @param employees
	 *            the employees to set
	 */
	public void setEmployees(final Set<EduPerson> employees) {
		this.employees = employees;
	}

	@Override
	public String getOrgNIN() {
		return orgNIN;
	}

}
