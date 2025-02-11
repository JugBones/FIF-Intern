package com.fifgroup.fifpractice.controller;

import com.fifgroup.fifpractice.model.User;
import com.fifgroup.fifpractice.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        logger.info("Fetching user with ID: {}", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            logger.error("User with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        logger.info("Fetching user with Email: {}", email);
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        logger.info("Creating user with ID: {}", user.getIdNumber());
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        logger.info("Updating user with ID: {}", id);
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setAddress(updatedUser.getAddress());
            user.setBirthDate(updatedUser.getBirthDate());
            logger.info("User with ID: {} updated successfully", id);
            return ResponseEntity.ok(userService.saveUser(user));
        } else {
            logger.error("User with ID: {} not found. Update failed", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        logger.warn("Attempting to delete user with ID: {}", id);
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            logger.info("Successfully deleted user with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.error("User with ID: {} not found. Deletion failed", id);
            return ResponseEntity.notFound().build();
        }
    }
}
