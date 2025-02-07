package com.fifgroup.fifpractice.controller;

import com.fifgroup.fifpractice.model.Equipment;
import com.fifgroup.fifpractice.model.User;
import com.fifgroup.fifpractice.service.EquipmentService;
import com.fifgroup.fifpractice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipmentControllerTest {

    @Mock
    private EquipmentService equipmentService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EquipmentController equipmentController;

    private Equipment equipment;
    private User user;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setIdNumber("12345");

        equipment = new Equipment();
        equipment.setId(1L);
        equipment.setType("Laptop");
        equipment.setColor("Black");
        equipment.setUser(user);

        file = new MockMultipartFile("file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, new byte[]{1, 2, 3});
    }

    @Test
    void testUploadEquipment_Success() {
        when(userService.getUserById("12345")).thenReturn(Optional.of(user));
        when(equipmentService.saveEquipment(any(Equipment.class), any(MultipartFile.class))).thenReturn(equipment);

        ResponseEntity<Equipment> response = equipmentController.uploadEquipment("12345", "Laptop", "Black", file);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Laptop", response.getBody().getType());
    }

    @Test
    void testUploadEquipment_UserNotFound() {
        when(userService.getUserById("12345")).thenReturn(Optional.empty());

        ResponseEntity<Equipment> response = equipmentController.uploadEquipment("12345", "Laptop", "Black", file);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testGetAllEquipment() {
        when(equipmentService.getEquipments("12345")).thenReturn(List.of(equipment));

        ResponseEntity<List<Equipment>> response = equipmentController.getAllEquipment("12345");

        assertEquals(200, response.getStatusCode().value());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void testGetEquipmentById_Success() {
        when(userService.getUserById("12345")).thenReturn(Optional.of(user));
        when(equipmentService.getEquipmentById(1L)).thenReturn(equipment);

        ResponseEntity<?> response = equipmentController.getEquipmentById("12345", 1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Laptop", ((Equipment) Objects.requireNonNull(response.getBody())).getType());
    }

    @Test
    void testGetEquipmentById_NotFound() {
        when(userService.getUserById("12345")).thenReturn(Optional.of(user));
        when(equipmentService.getEquipmentById(1L)).thenReturn(null);

        ResponseEntity<?> response = equipmentController.getEquipmentById("12345", 1L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testUpdateEquipment_Success() {
        when(userService.getUserById("12345")).thenReturn(Optional.of(user));
        when(equipmentService.updateEquipment(1L, "12345", "Tablet", "Silver", file)).thenReturn(equipment);

        ResponseEntity<Equipment> response = equipmentController.updateEquipment("12345", 1L, "Tablet", "Silver", file);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Laptop", response.getBody().getType());
    }

    @Test
    void testDeleteEquipment_Success() {
        when(userService.getUserById("12345")).thenReturn(Optional.of(user));
        when(equipmentService.getEquipmentById(1L)).thenReturn(equipment);

        ResponseEntity<Void> response = equipmentController.deleteEquipment("12345", 1L);

        assertEquals(204, response.getStatusCode().value());
        verify(equipmentService, times(1)).deleteEquipment(1L);
    }
}




// THIS IS MY FIRST TESTING WITH MOCKMVC TO VERIFY THE HTTP REQUEST AND BASICALLY THE MVC AND DIRECTLY TEST THE ENDPOINT
// I NOTICE THAT THIS TEST MORE LIKE INTEGRATION, AS IT IS TESTING THE BEHAVIOUR OF THE CONTROLLER
// GOT AN INSIGHT FROM KAK FERRY

//package com.fifgroup.fifpractice.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fifgroup.fifpractice.model.Equipment;
//import com.fifgroup.fifpractice.model.User;
//import com.fifgroup.fifpractice.service.EquipmentService;
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
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class EquipmentControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private EquipmentService equipmentService;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private EquipmentController equipmentController;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(equipmentController).build();
//    }
//
//    @Test
//    void testGetEquipmentById_Found() throws Exception {
//        User user = new User();
//        user.setIdNumber("1");
//        Equipment equipment = new Equipment();
//        equipment.setId(1L);
//        equipment.setType("Laptop");
//        equipment.setColor("Black");
//        equipment.setUser(user);
//
//        when(userService.getUserById("1")).thenReturn(Optional.of(user));
//        when(equipmentService.getEquipmentById(1L)).thenReturn(equipment);
//
//        mockMvc.perform(get("/users/1/equipment/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.type").value("Laptop"))
//                .andExpect(jsonPath("$.color").value("Black"));
//    }
//
//    @Test
//    void testGetEquipmentById_NotFound() throws Exception {
//        when(userService.getUserById("1")).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/users/1/equipment/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testCreateEquipment() throws Exception {
//        User user = new User();
//        user.setIdNumber("1");
//        Equipment equipment = new Equipment();
//        equipment.setId(1L);
//        equipment.setType("Laptop");
//        equipment.setColor("Black");
//
//        when(userService.getUserById("1")).thenReturn(Optional.of(user));
//        when(equipmentService.saveEquipment(any(Equipment.class))).thenReturn(equipment);
//
//        mockMvc.perform(post("/users/1/equipment")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(equipment)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.type").value("Laptop"))
//                .andExpect(jsonPath("$.color").value("Black"));
//    }
//}