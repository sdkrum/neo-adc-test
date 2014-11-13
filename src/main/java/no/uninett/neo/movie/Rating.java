package no.uninett.neo.movie;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;

@RelationshipEntity
public class Rating {

	@GraphId
	Long id;
}
