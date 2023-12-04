package io.github.jdouglas9025.doggolifeapi.configuration;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(
        basePackages = "io.github.jdouglas9025.doggolifeapi.repository.graph",
        transactionManagerRef = "reactiveTransactionManager"
)
@EnableTransactionManagement
public class Neo4jConfig extends Neo4jReactiveDataAutoConfiguration {
    //Retrieve values from properties file using @Value(${})
    @Value("${spring.neo4j.uri}")
    private String neo4jUri;
    @Value("${spring.neo4j.authentication.username}")
    private String username;
    @Value("${spring.neo4j.authentication.password}")
    private String password;

    @Bean
    public Driver getConfiguration() {
        return GraphDatabase.driver(neo4jUri, AuthTokens.basic(username, password));
    }

    @Bean(name = "reactiveTransactionManager")
    public ReactiveNeo4jTransactionManager reactiveNeo4jTransactionManager() {
        return new ReactiveNeo4jTransactionManager(getConfiguration());
    }
}
