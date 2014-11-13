package no.uninett.neo.movie;

import no.uninett.neo.person.Person;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

interface MovieRepository extends GraphRepository<Movie> {

	  @Query("start movie={0} match m<-[rating:RATED]-user return rating")
	  Iterable<Rating> getRatings(Movie movie);
	  Iterable<Person> findByActorsMoviesActorName(String name);
	}
