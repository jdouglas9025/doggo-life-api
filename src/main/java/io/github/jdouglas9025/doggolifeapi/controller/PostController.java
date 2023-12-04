package io.github.jdouglas9025.doggolifeapi.controller;

import io.github.jdouglas9025.doggolifeapi.entity.PostEntity;
import io.github.jdouglas9025.doggolifeapi.repository.graph.UserRepository;
import io.github.jdouglas9025.doggolifeapi.repository.relational.PostRepository;
import io.github.jdouglas9025.doggolifeapi.storage.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/getPosts")
    public ResponseEntity<List<PostEntity>> getPosts() {
        List<PostEntity> result = postRepository.findAll();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getPostsByUser")
    public ResponseEntity<List<PostEntity>> getPostsByUsername(@RequestParam String username) {
        List<PostEntity> result = postRepository.findByUsername(username);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //Use FilePart over MultiFilePart since using Webflux
    //Use RequestPart over RequestParam
    @PostMapping(value = "/createPost")
    public ResponseEntity<PostEntity> createPost(@RequestParam String username, @RequestParam String caption, @RequestPart("image") FilePart image) {
        //Upload image to S3
        String imageRef = S3Service.uploadObject(image);

        //Create post in database
        PostEntity post = postRepository.save(new PostEntity(username, caption, imageRef));

        //Return result and success message
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deletePost")
    public ResponseEntity<PostEntity> deletePost(@RequestParam Integer id) {
        Optional<PostEntity> post = postRepository.findById(id);

        //Check if post not found
        if (post.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //Delete image from S3
        String imageRef = post.get().getImageRef();
        S3Service.deleteObject(imageRef);

        //Delete post from DB
        postRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Get posts from users who this user follows
    @GetMapping(value = "/getPostsByFollowing")
    public ResponseEntity<List<PostEntity>> getPostsByFollowing(@RequestParam String username) {
        List<String> usernames = userRepository.findUsernamesByFollowing(username).collectList().block();

        List<PostEntity> posts = postRepository.findByUsernameIn(usernames);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    //Get posts from users who this user shares common interests with
    @GetMapping(value = "/getPostsByInterests")
    public ResponseEntity<List<PostEntity>> getPostsByInterests(@RequestParam String username) {
        List<String> usernames = userRepository.findUsernamesByInterests(username).collectList().block();

        List<PostEntity> posts = postRepository.findByUsernameIn(usernames);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}