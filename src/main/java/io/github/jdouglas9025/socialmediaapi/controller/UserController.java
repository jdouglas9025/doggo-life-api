package io.github.jdouglas9025.socialmediaapi.controller;

import io.github.jdouglas9025.socialmediaapi.entity.UserEntity;
import io.github.jdouglas9025.socialmediaapi.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController includes @ResponseBody annotation implicitly to return method responses as a response body (e.g., JSON)
//@RequestMapping allows us to specify an additional URI base path for all requests in this class
@RestController
@RequestMapping("/users")
public class UserController {

    //Inject instance of repository interface to call methods on DB
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Endpoints
    @GetMapping("/getUsers")
    public Flux<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/getUser/{username}")
    public Mono<UserEntity> getUser(@PathVariable String username) {
        return userRepository.findById(username);
    }

    @GetMapping("/getFollowers/{username}")
    public Flux<UserEntity> getFollowers(@PathVariable String username) {
        return userRepository.findFollowers(username);
    }

    @PostMapping("/createUser")
    public Mono<UserEntity> createUser(@RequestParam String username, @RequestParam Integer age) {
        UserEntity userEntity = new UserEntity(username, age);
        //Creates the user and returns it as a response
        return userRepository.save(userEntity);
    }

    @PostMapping("/followUser")
    public Mono<String> followUser(@RequestParam String source, String target) {
        return userRepository.saveFollow(source, target);
    }

    @PostMapping("/likeInterest")
    public Mono<String> likeInterest(@RequestParam String username, String interest) {
        return userRepository.saveLike(username, interest);
    }
}
