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
    private Integer age;
    //Relationships
    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private final List<UserEntity> following = new ArrayList<>();
    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private final List<InterestEntity> likes = new ArrayList<>();

    public UserEntity(String username, Integer age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<UserEntity> getFollowing() {
        return following;
    }

    public List<InterestEntity> getLikes() {
        return likes;
    }
}
