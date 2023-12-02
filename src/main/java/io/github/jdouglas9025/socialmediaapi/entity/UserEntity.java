package io.github.jdouglas9025.socialmediaapi.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("User")
public class UserEntity {
    //Attributes
    @Id
    private final String username;
    private final String name;
    private final String breed;
    private final Long age;
    //Relationships
    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private final List<UserEntity> following;
    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private final List<InterestEntity> likes;

    //Initialize all fields upon construction (including lists)
    public UserEntity(String username, String name, String breed, Long age, List<UserEntity> following, List<InterestEntity> likes) {
        this.username = username;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.following = following;
        this.likes = likes;
    }

    //Need getter() methods for parsing
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public Long getAge() {
        return age;
    }

    public List<UserEntity> getFollowing() {
        return following;
    }

    public List<InterestEntity> getLikes() {
        return likes;
    }
}
