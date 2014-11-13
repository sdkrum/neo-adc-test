package no.uninett.adc.neo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class EntitlementChange extends Change {

	private Entitlement entitlement;
	private Set<String> systemURN = new HashSet<String>(2);

	public EntitlementChange(final ChangeOperation operation,
			final Entitlement value) {
		setChangeOperation(operation);
		setEntitlement(value);
		setDateChanged(new Date());
	}

	public EntitlementChange(final ChangeOperation operation,
			final Entitlement value, final Date dateChanged) {
		setChangeOperation(operation);
		setEntitlement(value);
		setDateChanged(dateChanged);
	}

	public Entitlement getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(final Entitlement value) {
		this.entitlement = value;
	}

	/**
	 * @return the systemURN
	 */
	public Set<String> getSystemURN() {
		return systemURN;
	}

	/**
	 * @param systemURN
	 *            the systemURN to set
	 */
	public void setSystemURN(Set<String> systemURN) {
		this.systemURN = systemURN;
	}

	@Override
	public String toString() {
		return "EC: ENT : " + getEntitlement().getEntitlement() + ", Op : "
				+ getChangeOperation().toString();
	}
}
