package com.fifgroup.fifpractice.controller;

import com.fifgroup.fifpractice.model.User;
import com.fifgroup.fifpractice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_Found() {
        User user = new User();
        user.setIdNumber("123");
        when(userService.getUserById("123")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById("123");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("123", response.getBody().getIdNumber());
        verify(userService, times(1)).getUserById("123");
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById("123")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById("123");

        assertEquals(404, response.getStatusCode().value());
        verify(userService, times(1)).getUserById("123");
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setIdNumber("123");
        when(userService.saveUser(user)).thenReturn(user);

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals("123", createdUser.getIdNumber());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void testUpdateUser_Found() {
        User existingUser = new User();
        existingUser.setIdNumber("123");
        existingUser.setName("Old Name");

        User updatedUser = new User();
        updatedUser.setName("New Name");

        when(userService.getUserById("123")).thenReturn(Optional.of(existingUser));
        when(userService.saveUser(any(User.class))).thenReturn(existingUser);

        ResponseEntity<User> response = userController.updateUser("123", updatedUser);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("New Name", Objects.requireNonNull(response.getBody()).getName());
        verify(userService, times(1)).getUserById("123");
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        User updatedUser = new User();
        updatedUser.setName("New Name");

        when(userService.getUserById("123")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser("123", updatedUser);

        assertEquals(404, response.getStatusCode().value());
        verify(userService, times(1)).getUserById("123");
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    void testDeleteUser_Found() {
        when(userService.getUserById("123")).thenReturn(Optional.of(new User()));

        ResponseEntity<Void> response = userController.deleteUser("123");

        assertEquals(204, response.getStatusCode().value());
        verify(userService, times(1)).deleteUser("123");
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userService.getUserById("123")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = userController.deleteUser("123");

        assertEquals(404, response.getStatusCode().value());
        verify(userService, never()).deleteUser(anyString());
    }
}




// THIS IS MY FIRST TESTING WITH MOCKMVC TO VERIFY THE HTTP REQUEST AND BASICALLY THE MVC AND DIRECTLY TEST THE ENDPOINT
// I NOTICE THAT THIS TEST MORE LIKE INTEGRATION, AS IT IS TESTING THE BEHAVIOUR OF THE CONTROLLER
// GOT AN INSIGHT FROM KAK FERRY

//package com.fifgroup.fifpractice.controller;
//
//import com.fifgroup.fifpractice.model.User;
//import com.fifgroup.fifpractice.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    void testGetAllUsers() throws Exception {
//        User user1 = new User();
//        user1.setIdNumber("1");
//        user1.setName("John Doe");
//        user1.setAddress("123 Street");
//        user1.setBirthDate(LocalDate.parse("1990-01-01"));
//        User user2 = new User();
//        user2.setIdNumber("2");
//        user2.setName("Jane Doe");
//        user2.setAddress("456 Avenue");
//        user2.setBirthDate(LocalDate.parse("1992-02-02"));
//        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].idNumber").value("1"))
//                .andExpect(jsonPath("$[1].idNumber").value("2"));
//    }
//
//    @Test
//    void testGetUserById() throws Exception {
//        User user = new User();
//        user.setIdNumber("1");
//        user.setName("John Doe");
//        user.setAddress("123 Street");
//        user.setBirthDate(LocalDate.parse("1990-01-01"));
//        when(userService.getUserById("1")).thenReturn(Optional.of(user));
//
//        mockMvc.perform(get("/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.idNumber").value("1"));
//    }
//
//    @Test
//    void testGetUserById_NotFound() throws Exception {
//        when(userService.getUserById("99")).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/users/99"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testCreateUser() throws Exception {
//        User user = new User();
//        user.setIdNumber("1");
//        user.setName("John Doe");
//        user.setAddress("123 Street");
//        user.setBirthDate(LocalDate.parse("1990-01-01"));
//        when(userService.saveUser(any(User.class))).thenReturn(user);
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"idNumber\":\"1\",\"name\":\"John Doe\",\"address\":\"123 Street\",\"birthDate\":\"1990-01-01\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.idNumber").value("1"));
//    }
//}