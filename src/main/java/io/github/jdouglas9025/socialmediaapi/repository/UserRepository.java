package io.github.jdouglas9025.socialmediaapi.repository;

import io.github.jdouglas9025.socialmediaapi.entity.UserEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import reactor.core.publisher.Mono;

//Data type is UserEntity with primary key of type Long
public interface UserRepository extends ReactiveNeo4jRepository<UserEntity, String> {
    //Inherits basic boilerplate methods such as save() and findAll()

    //Specify request inputs into query using $
    @Query("MATCH (source:User), (target:User) " +
            "WHERE source.username = $source AND target.username = $target " +
            "CREATE (source)-[:FOLLOWS]->(target) " +
            "RETURN source")
    Mono<UserEntity> saveFollow(String source, String target);

    @Query("MATCH (user:User), (interest:Interest) " +
            "WHERE user.username = $username AND interest.value = $interest " +
            "CREATE (user)-[:LIKES]->(interest) " +
            "RETURN user")
    Mono<UserEntity> saveLike(String username, String interest);
}
