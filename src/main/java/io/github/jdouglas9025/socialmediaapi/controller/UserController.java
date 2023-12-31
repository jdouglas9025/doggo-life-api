package io.github.jdouglas9025.socialmediaapi.controller;

import io.github.jdouglas9025.socialmediaapi.entity.UserEntity;
import io.github.jdouglas9025.socialmediaapi.repository.graph.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController includes @ResponseBody implicitly to return method responses as a response body (e.g., JSON)
//@RequestMapping allows us to specify an additional URI base path for all requests in this class
@RestController
@RequestMapping("/users")
public class UserController {
    //Note: for all requests, Spring has already confirmed that user's JWT token is valid by checking relevant JWT properties through Google
    //In a production application, we would also want to verify that the caller is in a trusted list of users
    //Inject instance of graph repository interface to call methods on DB
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/getUsers")
    public Flux<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/getUser")
    public Mono<UserEntity> getUser(@RequestParam String username) {
        return userRepository.findOneByUsername(username);
    }

    @GetMapping("/getFollowers")
    public Flux<UserEntity> getFollowers(@RequestParam String username) {
        return userRepository.findFollowers(username);
    }

    @PostMapping("/updateUser")
    public Mono<UserEntity> updateUser(@RequestBody UserEntity userEntity) {
        //Update user (match based on userId) with new values
        return userRepository.save(userEntity);
    }

    @PostMapping("/followUser")
    public Mono<String> followUser(@RequestParam String source, @RequestParam String target) {
        return userRepository.saveFollow(source, target);
    }

    @PostMapping("/likeInterest")
    public Mono<String> likeInterest(@RequestParam String username, @RequestParam String interest) {
        return userRepository.saveLike(username, interest);
    }
}
