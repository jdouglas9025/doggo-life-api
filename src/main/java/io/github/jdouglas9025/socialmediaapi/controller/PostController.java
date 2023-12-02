package io.github.jdouglas9025.socialmediaapi.controller;

import io.github.jdouglas9025.socialmediaapi.entity.PostEntity;
import io.github.jdouglas9025.socialmediaapi.repository.relational.PostRepository;
import io.github.jdouglas9025.socialmediaapi.storage.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
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
}
