package io.github.jdouglas9025.socialmediaapi.repository;

import io.github.jdouglas9025.socialmediaapi.entity.UserEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Data type is UserEntity with primary key of type Long
//Extended repo provides many boilerplate methods such as save() and findAll()
@Repository
public interface UserRepository extends ReactiveNeo4jRepository<UserEntity, String> {
    //Derived method implementation based on findOne()
    Mono<UserEntity> findOneByUsername(String username);

    //Specify request inputs into query using $
    @Query("MATCH (source:User), (target:User) " +
            "WHERE source.username = $source AND target.username = $target " +
            "CREATE (source)-[:FOLLOWS]->(target) " +
            "RETURN \"Success\"")
    Mono<String> saveFollow(String source, String target);

    @Query("MATCH (user:User), (interest:Interest) " +
            "WHERE user.username = $username AND interest.name = $interest " +
            "CREATE (user)-[:LIKES]->(interest) " +
            "RETURN \"Success\"")
    Mono<String> saveLike(String username, String interest);

    //The Path data type allows us to fully explore relationships and nodes to return a comprehensive view
    //collect() allows us to return all nodes/relationships into a single list
    @Query("MATCH path=(:User {username: $username})<--(follower) " +
            "RETURN follower, collect(nodes(path)), collect(relationships(path))")
    Flux<UserEntity> findFollowers(String username);
}
