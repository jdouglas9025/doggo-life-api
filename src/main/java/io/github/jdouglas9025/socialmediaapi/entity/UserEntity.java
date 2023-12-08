package io.github.jdouglas9025.socialmediaapi.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("User")
public class UserEntity {
    //Immutable attributes
    @Id
    private String userId;

    //Profile attributes
    private String username;
    private String name;
    private String breed;
    private Long age;

    //Relationships
    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<UserEntity> following;
    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private List<InterestEntity> likes;

    //Initialize all fields upon construction (including lists)
    public UserEntity(String userId, String username, String name, String breed, Long age, List<UserEntity> following, List<InterestEntity> likes) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.following = following;
        this.likes = likes;
    }

    //Default constructor with empty values
    public UserEntity() {
        this.userId = "";
        this.username = "";
        this.name = "";
        this.breed = "";
        this.age = 0L;
        this.following = new ArrayList<>();
        this.likes = new ArrayList<>();
    }

    //Getters/Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public List<UserEntity> getFollowing() {
        return following;
    }

    public void setFollowing(List<UserEntity> following) {
        this.following = following;
    }

    public List<InterestEntity> getLikes() {
        return likes;
    }

    public void setLikes(List<InterestEntity> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", following=" + following +
                ", likes=" + likes +
                '}';
    }
}
