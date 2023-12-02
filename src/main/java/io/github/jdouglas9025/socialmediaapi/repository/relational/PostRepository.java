package io.github.jdouglas9025.socialmediaapi.repository.relational;

import io.github.jdouglas9025.socialmediaapi.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    //Derived method implementation based on findBy()
    List<PostEntity> findByUsername(String username);
}
