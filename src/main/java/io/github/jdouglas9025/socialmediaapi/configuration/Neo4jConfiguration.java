package io.github.jdouglas9025.socialmediaapi.configuration;

import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider;
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager;

//Configuration file for setting up Bean settings
@Configuration(proxyBeanMethods = false)
public class Neo4jConfiguration {
    //Declare ReactiveTransactionManager bean since not auto-configured per docs
    @Bean
    public ReactiveNeo4jTransactionManager reactiveTransactionManager(Driver driver, ReactiveDatabaseSelectionProvider provider) {
        return new ReactiveNeo4jTransactionManager(driver, provider);
    }

    //Use Neo4j 5 dialect explicitly per docs
    @Bean
    org.neo4j.cypherdsl.core.renderer.Configuration cypherDslConfiguration() {
        return org.neo4j.cypherdsl.core.renderer.Configuration.newConfig()
                .withDialect(Dialect.NEO4J_5).build();
    }
}
