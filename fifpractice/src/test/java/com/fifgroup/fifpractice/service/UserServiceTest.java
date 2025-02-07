package com.fifgroup.fifpractice.service;

import com.fifgroup.fifpractice.model.User;
import com.fifgroup.fifpractice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User()));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById("123")).thenReturn(Optional.of(new User()));

        Optional<User> user = userService.getUserById("123");

        assertTrue(user.isPresent());
        verify(userRepository, times(1)).findById("123");
    }

    @Test
    void testSaveUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById("123")).thenReturn(true);

        userService.deleteUser("123");

        verify(userRepository, times(1)).deleteById("123");
    }
}





//package com.fifgroup.fifpractice.service;
//
//import com.fifgroup.fifpractice.model.User;
//import com.fifgroup.fifpractice.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    void testGetAllUsers() {
//        User user = new User();
//        user.setIdNumber("654321");
//        user.setName("John Doe");
//        user.setAddress("Jakarta");
//        user.setBirthDate(LocalDate.parse("1995-06-15"));
//        List<User> users = List.of(user);
//        when(userRepository.findAll()).thenReturn(users);
//
//        List<User> result = userService.getAllUsers();
//        assertEquals(1, result.size());
//        assertEquals("John Doe", result.get(0).getName());
//    }
//
//    @Test
//    void testSaveUser() {
//        User user = new User();
//        user.setIdNumber("654321");
//        user.setName("Jane Doe");
//        user.setAddress("Bandung");
//        user.setBirthDate(LocalDate.parse("1997-08-25"));
//        when(userRepository.save(user)).thenReturn(user);
//
//        User savedUser = userService.saveUser(user);
//        assertNotNull(savedUser);
//        assertEquals("Jane Doe", savedUser.getName());
//    }
//}
