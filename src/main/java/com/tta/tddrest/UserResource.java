package com.tta.tddrest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static java.util.Optional.ofNullable;

@RestController
public class UserResource {

    InMemoryUserService userService = new InMemoryUserService();

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<User> get(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        if (ofNullable(user).isPresent()) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        if (ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User createdUser = userService.create(user);
        URI location = UriComponentsBuilder.fromPath("/user/{userId}")
                .buildAndExpand(createdUser.getUserId())
                .toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

}
