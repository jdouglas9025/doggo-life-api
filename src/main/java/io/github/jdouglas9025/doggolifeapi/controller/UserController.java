package io.github.jdouglas9025.doggolifeapi.controller;

import io.github.jdouglas9025.doggolifeapi.entity.UserEntity;
import io.github.jdouglas9025.doggolifeapi.repository.graph.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController includes @ResponseBody implicitly to return method responses as a response body (e.g., JSON)
//@RequestMapping allows us to specify an additional URI base path for all requests in this class
@RestController
@RequestMapping("/users")
public class UserController {
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

    @PostMapping("/createUser")
    public Mono<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        //Creates the user and returns it as a response
        return userRepository.save(userEntity);
    }

    @PostMapping("/followUser")
    public Flux<UserEntity> followUser(@RequestParam String source, String target) {
        return userRepository.saveFollow(source, target);
    }

    @PostMapping("/likeInterest")
    public Flux<UserEntity> likeInterest(@RequestParam String username, String interest) {
        return userRepository.saveLike(username, interest);
    }
}
