package no.uninett.neo.movie;

import java.util.Set;

import no.uninett.neo.person.Person;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Movie {

  @GraphId Long id;

  @Indexed (indexType = IndexType.FULLTEXT, indexName = "search")
  String title;

  Person director;

  @RelatedTo(type="ACTS_IN", direction = Direction.INCOMING)
  Set<Person> actors;

  @RelatedToVia(type = "RATED")
  Iterable<Rating> ratings;

  @Query("start movie=node({self}) match movie-->genre<--similar return similar")
  Iterable<Movie> similarMovies;
}
