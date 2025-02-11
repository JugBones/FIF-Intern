package com.fifgroup.fifpractice.service;

import com.fifgroup.fifpractice.model.User;
import com.fifgroup.fifpractice.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger logger = LogManager.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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

    public Optional<User> getUserByEmail(String email) {
        logger.info("Fetching user with Email: {}", email);
        return userRepository.findByEmail(email);
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

    /**
     * Loads user details for authentication by email (which acts as the username).
     * This method is used by Spring Security for authentication.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
    }
}
