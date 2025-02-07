package com.fifgroup.fifpractice.service;

import com.fifgroup.fifpractice.model.User;
import com.fifgroup.fifpractice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        logger.info("Fetching user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            logger.error("User with ID: {} not found", id);
            return Optional.empty();
        }
    }

    public User saveUser(User user) {
        logger.info("Saving user with ID: {}", user.getIdNumber());
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        logger.warn("Attempting to delete user with ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("Successfully deleted user with ID: {}", id);
        } else {
            logger.error("Failed to delete user. User with ID: {} not found", id);
        }
    }
}
