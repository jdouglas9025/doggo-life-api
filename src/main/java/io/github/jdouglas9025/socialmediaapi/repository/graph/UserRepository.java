package io.github.jdouglas9025.socialmediaapi.repository.graph;

import io.github.jdouglas9025.socialmediaapi.entity.UserEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveNeo4jRepository<UserEntity, String> {
    /**
     * This derived method finds a user based on their immutable user ID
     *
     * @param userId the userId of the target user
     * @return a Mono object of type UserEntity representing the user
     */
    Mono<UserEntity> findOneByUserId(String userId);

    /**
     * This derived method finds a user based on their username
     *
     * @param username the username of the target user
     * @return a Mono object of type UserEntity representing the user
     */
    Mono<UserEntity> findOneByUsername(String username);

    /**
     * Creates a "FOLLOWS" relationship from the source user to the target user
     *
     * @param source the username of the source (originating) user
     * @param target the username of the target user
     * @return a Mono of type String with a success message
     */
    @Query("MATCH (source:User), (target:User) " +
            "WHERE source.username = $source AND target.username = $target " +
            "CREATE (source)-[:FOLLOWS]->(target) " +
            "RETURN 'Success'")
    Mono<String> saveFollow(String source, String target);

    /**
     * Creates a "LIKES" relationship from the user to the interest
     *
     * @param username the username of the user
     * @param interest the name of the interest
     * @return a Mono of type String with a success message
     */
    @Query("MATCH (user:User), (interest:Interest) " +
            "WHERE user.username = $username AND interest.name = $interest " +
            "CREATE (user)-[:LIKES]->(interest) " +
            "RETURN 'Success'")
    Mono<String> saveLike(String username, String interest);

    /**
     * Retrieves the list of users who are following the specified user
     *
     * @param username the username of the user
     * @return a Flux of type UserEntity representing the user's followers
     */
    @Query("MATCH path=(source:User {username: $username})<-[:FOLLOWS]-(follower) " +
            "RETURN follower, collect(nodes(path)), collect(relationships(path))")
    Flux<UserEntity> findFollowers(String username);

    /**
     * Retrieves the list of users who the specified user is following
     *
     * @param username the username of the user
     * @return a Flux of type String representing the usernames of users who the specified user is following
     */
    @Query("MATCH (source:User {username: $username})-[:FOLLOWS]->(following) " +
            "RETURN following.username")
    Flux<String> findUsernamesByFollowing(String username);

    /**
     * Retrieves a list of users who like the same interests as the specified user
     *
     * @param username the username of the user
     * @return a Flux of type String representing the usernames of users who share a common interest with the specified user
     */
    @Query("MATCH (source:User {username: $username})-[:LIKES]->(interest)<-[:LIKES]-(match:User) " +
            "RETURN match.username")
    Flux<String> findUsernamesByInterests(String username);
}
