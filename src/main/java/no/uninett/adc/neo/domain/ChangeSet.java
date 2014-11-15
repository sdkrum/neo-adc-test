package no.uninett.adc.neo.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class ChangeSet {

	@GraphId
	private Long id;

	@GraphProperty(propertyType = Integer.class)
	private ChangeOperation operation;

	@RelatedTo(type = RelationshipType.CHANGESET_FOR_OBJECT, direction = Direction.OUTGOING)
	private EduObject eduObject;

	@RelatedTo(type = RelationshipType.INCLUDES_CHANGE, direction = Direction.OUTGOING)
	private Set<Change> changes = new HashSet<Change>();

	@RelatedTo(type = RelationshipType.HAS_STATE, direction = Direction.OUTGOING)
	@Fetch
	private Set<ChangeState> changeStates = new HashSet<ChangeState>();

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public ChangeOperation getOperation() {
		return operation;
	}

	public void setOperation(final ChangeOperation operation) {
		this.operation = operation;
	}

	public Set<Change> getChanges() {
		return changes;
	}

	public void setChanges(final Set<Change> changes) {
		this.changes = changes;
	}

	public Set<ChangeState> getChangeStates() {
		return changeStates;
	}

	public void setChangeStates(final Set<ChangeState> changeStates) {
		this.changeStates = changeStates;
	}

	public EduObject getEduObject() {
		return eduObject;
	}

	public void setEduObject(final EduObject eduObject) {
		this.eduObject = eduObject;
	}

	public List<ChangeState> getWaitingChangeStates() {
		final Collection<ChangeState> allStates = getChangeStates();
		final List<ChangeState> waitingStates = new ArrayList<ChangeState>();
		for (final ChangeState state : allStates) {
			if (state.getStateCode().equals(ChangeStateCode.PENDING)
					|| state.getStateCode().equals(ChangeStateCode.DELIVERED)
					|| state.getStateCode().equals(ChangeStateCode.ERROR)) {
				waitingStates.add(state);
			}
		}
		return waitingStates;
	}

	public boolean is(final ChangeOperation co) {
		return getOperation().equals(co);
	}

	@Override
	public String toString() {
		return "Changeset for: " + eduObject + ": Changes:" + changes.toString() + " ChangeStates: "
				+ changeStates.toString() + " ChangeOperation: " + getOperation().toString();
	}
}
