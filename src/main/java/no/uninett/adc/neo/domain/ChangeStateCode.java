package no.uninett.adc.neo.domain;

public enum ChangeStateCode {
    // Waiting means waits for aknowledgement from the admins of the given institution
	WAITING,
	// Lingering around waiting for the next run of the data transfer into the systems
	PENDING,
	// The message for this change set is sent out in the world and waits for feedback
	DELIVERED,
	// someone tried to import this case already but failed
	ERROR,
	// someone tried to import this case already but failed partly
	WARN, 
	// this one signals that the changeset was sucessfully exported to the system
	SUCCESS 
}
