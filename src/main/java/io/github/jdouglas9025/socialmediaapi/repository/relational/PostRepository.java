package io.github.jdouglas9025.socialmediaapi.repository.relational;

import io.github.jdouglas9025.socialmediaapi.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    /**
     * This derived method finds all posts associated with the specified user
     *
     * @param username the username of the user
     * @return a list of PostEntity objects representing all the user's posts
     */
    List<PostEntity> findByUsername(String username);

    /**
     * This derived method finds all posts associated with the specified list of users
     *
     * @param usernames the usernames of the users
     * @return a list of PostEntity objects representing all posts for the users
     */
    List<PostEntity> findByUsernameIn(List<String> usernames);
}
