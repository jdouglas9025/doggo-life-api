package io.github.jdouglas9025.socialmediaapi.controller;

import io.github.jdouglas9025.socialmediaapi.entity.PostEntity;
import io.github.jdouglas9025.socialmediaapi.repository.graph.UserRepository;
import io.github.jdouglas9025.socialmediaapi.repository.relational.PostRepository;
import io.github.jdouglas9025.socialmediaapi.storage.S3Service;
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
    //Note: for all requests, Spring has already confirmed that user's JWT token is valid by checking relevant JWT properties through Google
    //In a production application, we would also want to verify that the caller is in a trusted list of users
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
    public ResponseEntity<PostEntity> createPost(@RequestParam String userId, @RequestParam String username, @RequestParam String caption, @RequestPart("image") FilePart image) {
        //Upload image to S3
        String imageRef = S3Service.uploadObject(image);

        //Create post in database
        PostEntity post = postRepository.save(new PostEntity(userId, username, caption, imageRef));

        //Return result and success message
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deletePost")
    public ResponseEntity<String> deletePost(@RequestParam Integer id) {
        Optional<PostEntity> container = postRepository.findById(id);

        //Check if post not found
        if (container.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PostEntity post = container.get();

        //Delete image from S3
        String imageRef = post.getImageRef();
        S3Service.deleteObject(imageRef);

        //Delete post from DB
        postRepository.delete(post);

        return new ResponseEntity<>("Success", HttpStatus.OK);
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