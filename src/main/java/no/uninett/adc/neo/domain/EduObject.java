package no.uninett.adc.neo.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public abstract class EduObject {

	@GraphId
	private Long id;

	private boolean active = true;

	@RelatedTo(type = "HAS_ATTRIBUTE", direction = Direction.OUTGOING)
	@Fetch
	Set<Attribute> attributes;

	public static ChangeSet diff(EduObject old, EduObject neW) {
		if (neW == null || (!neW.isActive() && old.isActive())) {
			return old.diffForDelete();
		}
		if (old == null || (!old.isActive() && neW.isActive())) {
			return neW.diffForCreate();
		}
		return old.diffForUpdate(neW);
	}

	private ChangeSet diffForDelete() {
		Set<Change> changes = new HashSet<Change>();
		// for (Attribute att : attributes) {
		// changes.add(new AttributeChange(ChangeOperation.CREATE, att));
		// }
		ChangeSet ret = makeChangeSet(this, changes, ChangeOperation.DELETE);
		this.active = false;
		return ret;
	}

	private ChangeSet diffForCreate() {
		Set<Change> changes = new HashSet<Change>();
		// we do not do that until someone really asks for it...
		//
		// for (Attribute att : attributes) {
		// changes.add(new AttributeChange(ChangeOperation.CREATE, att));
		// }
		ChangeSet ret = makeChangeSet(this, changes, ChangeOperation.CREATE);
		this.active = true;
		return ret;
	}

	private ChangeSet diffForUpdate(EduObject theNew) {
		Set<Change> changes = new HashSet<Change>();
		boolean found = false;
		for (Attribute att : attributes) {
			for (Attribute oAtt : theNew.getAttributes()) {
				if (att.getKey().equals(oAtt.getKey())) {
					if (!att.valueEquals(oAtt)) {
						changes.add(new AttributeChange(ChangeOperation.UPDATE,
								oAtt));
					}
					found = true;
					break;
				}
			}
			if (!found) {
				changes.add(new AttributeChange(ChangeOperation.DELETE, att));
			} else {
				found = false;
			}
		}
		for (Attribute oAtt : theNew.getAttributes()) {
			for (Attribute att : getAttributes()) {
				if (att.getKey().equals(oAtt.getKey())) {
					found = true;
					break;
				}
			}
			if (!found) {
				changes.add(new AttributeChange(ChangeOperation.CREATE, oAtt));
			} else {
				found = false;
			}
		}
		if (changes.isEmpty()) {
			return null;
		}
		ChangeSet ret = makeChangeSet(theNew, changes, ChangeOperation.UPDATE);
		return ret;
	}

	private ChangeSet makeChangeSet(EduObject obj, Set<Change> changes,
			ChangeOperation op) {
		ChangeSet ret = new ChangeSet();
		ret.setChanges(changes);
		ret.setOperation(op);
		ret.setEduObject(obj);
		return ret;
	}

	public void addAttribute(final Attribute val) {
		val.setObject(this);
		getAttributes().add(val);
	}

	public void addAttribute(final String key, final String value) {
		final Attribute val = new Attribute(key, value);
		addAttribute(val);
	}

	public void removeAttibute(final String key) {
		for (final Attribute val : getAttributes()) {
			if (val.getKey().equals(key)) {
				getAttributes().remove(val);
			}
		}
	}

	public void removeAttibute(final String key, final String value) {
		for (final Attribute val : getAttributes()) {
			if (val.getKey().equals(key) && val.valueEquals(value)) {
				getAttributes().remove(val);
			}
		}
	}

	public Set<Attribute> getAttributes() {
		if (attributes == null) {
			synchronized (this) {
				if (attributes == null) {
					attributes = new HashSet<Attribute>(10);
				}
			}
		}
		return attributes;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

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
		if (!(getClass().equals(obj.getClass()))) {
			return false;
		}
		final EduObject other = (EduObject) obj;

		if (getId() == null || other.getId() == null) {
			// a non-persistent entry causes trouble, so no equals here...
			return false;
		}
		if (getId().equals(other.getId())) {
			return true;
		}
		return false;
	}

	public abstract String getOrgNIN();

}
