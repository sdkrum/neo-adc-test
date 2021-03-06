package no.uninett.adc.neo.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class EduPerson extends EduObject {

	@Indexed(unique = true)
	String feideId;
	@RelatedTo(type = RelationshipType.IS_ENTITLET, direction = Direction.OUTGOING)
	@Fetch
	Set<Entitlement> entitlements;
	@RelatedTo(type = RelationshipType.WORKS_FOR_ORG, direction = Direction.OUTGOING)
	@Fetch
	EduOrg org;

	@Deprecated
	public EduPerson() {

	}

	public EduPerson(final String feide) {
		feideId = feide;
	}

	public ChangeSet diff() {
		ChangeSet atts = EduObject.diff(null, this);
		final Set<Change> ents = diffEnt();
		atts = merge(atts, ents);
		return atts;
	}

	public ChangeSet diff(final EduPerson neW) {
		ChangeSet atts = EduObject.diff(this, neW);
		final Set<Change> ents = diffEnt(neW);
		atts = merge(atts, ents);
		return atts;
	}

	private ChangeSet merge(final ChangeSet atts, final Set<Change> ents) {
		ChangeSet ret = atts;
		if (ents != null && !ents.isEmpty()) {
			if (ret == null) {
				ret = new ChangeSet();
				ret.setEduObject(this);
				ret.setOperation(ChangeOperation.UPDATE);
				ret.setChanges(ents);
			} else {
				ret.getChanges().addAll(ents);
			}
		}
		return ret;
	}

	private Set<Change> diffEnt() {
		final Set<Change> changes = new HashSet<Change>(2);
		for (final Entitlement ent : getEntitlements()) {
			changes.add(new EntitlementChange(ChangeOperation.CREATE, ent));
		}
		return changes;
	}

	private Set<Change> diffEnt(final EduPerson neW) {
		Set<Entitlement> newEnt = null;
		final Set<Change> changes = new HashSet<Change>(2);
		if ( neW == null ) {
			newEnt = Collections.emptySet();
		} else {
			newEnt = neW.getEntitlements();
		}
		for (final Entitlement ent : getEntitlements()) {
			if (!newEnt.contains(ent)) {
				changes.add(new EntitlementChange(ChangeOperation.DELETE, ent));
			}
		}
		for (final Entitlement ent : newEnt) {
			final Set<Entitlement> oldEnt = getEntitlements();
			if (!oldEnt.contains(ent)) {
				changes.add(new EntitlementChange(ChangeOperation.CREATE, ent));
			}
		}
		return changes;
	}

	/**
	 * @return the feideId
	 */
	public String getFeideId() {
		return feideId;
	}

	/**
	 * @param feideId
	 *            the feideId to set
	 */
	public void setFeideId(final String feideId) {
		this.feideId = feideId;
	}

	/**
	 * @return the entitlements
	 */
	public Set<Entitlement> getEntitlements() {
		if (entitlements == null) {
			entitlements = new HashSet<Entitlement>(5);
		}
		return entitlements;
	}

	/**
	 * @param entitlements
	 *            the entitlements to set
	 */
	public void setEntitlements(final Set<Entitlement> entitlements) {
		this.entitlements = entitlements;
	}

	public void add(final Entitlement ent) {
		if (entitlements == null) {
			entitlements = new HashSet<Entitlement>(4);
		}
		entitlements.add(ent);
	}

	public boolean remove(final Entitlement ent) {
		if (entitlements == null) {
			return false;
		}
		return entitlements.remove(ent);
	}

	/**
	 * @return the org
	 */
	public EduOrg getOrg() {
		return org;
	}

	/**
	 * @param org
	 *            the org to set
	 */
	public void setOrg(final EduOrg org) {
		this.org = org;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? System.identityHashCode(this) : getId()
				.hashCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EduPerson)) {
			return false;
		}
		final EduPerson other = (EduPerson) obj;

		if (getId() == null || other.getId() == null) {
			// a non-persistent entry causes trouble, so no equals here...
			return false;
		}
		if (getId().equals(other.getId())) {
			return true;
		}
		return false;
	}

	@Override
	public String getOrgNIN() {
		return getOrg().getOrgNIN();
	}

}
