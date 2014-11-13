package no.uninett.adc.neo.dao;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

@Configuration
@ComponentScan(basePackages = "no.uninett.adc.neo.domain")
@EnableNeo4jRepositories(basePackages = "no.uninett.adc.neo.dao")
public class AppConfigTest extends Neo4jConfiguration {

	public AppConfigTest() {
		setBasePackage("no.uninett.adc.neo.domain");
	}

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		return new TestGraphDatabaseFactory().newImpermanentDatabase();
	}

	// @Bean
	// public ConversionServiceFactoryBean conversionService() {
	// final Set converters = new HashSet();
	//
	// converters.add(new IntegerToChangeOperation());
	// converters.add(new IntegerToChangeStateCode());
	//
	// converters.add(new ChangeOperationToInteger());
	// converters.add(new ChangeStateCodeToInteger());
	//
	// final ConversionServiceFactoryBean bean = new
	// ConversionServiceFactoryBean();
	// bean.setConverters(converters);
	// return bean;
	// }
}