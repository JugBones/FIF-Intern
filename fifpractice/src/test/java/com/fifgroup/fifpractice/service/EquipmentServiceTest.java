package com.fifgroup.fifpractice.service;

import com.fifgroup.fifpractice.model.Equipment;
import com.fifgroup.fifpractice.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private EquipmentService equipmentService;

    private Equipment equipment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        equipment = new Equipment();
        equipment.setId(1L);
        equipment.setType("Laptop");
        equipment.setColor("Black");
    }

    @Test
    void testSaveEquipment() throws IOException {
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(file.getOriginalFilename()).thenReturn("test.pdf");
        when(file.getContentType()).thenReturn("application/pdf");
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        Equipment savedEquipment = equipmentService.saveEquipment(equipment, file);

        assertNotNull(savedEquipment);
        assertEquals("Laptop", savedEquipment.getType());
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
    }

    @Test
    void testGetEquipments() {
        when(equipmentRepository.findByUserIdNumber("12345")).thenReturn(Arrays.asList(equipment));

        var result = equipmentService.getEquipments("12345");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(equipmentRepository, times(1)).findByUserIdNumber("12345");
    }

    @Test
    void testGetEquipmentById() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        Equipment foundEquipment = equipmentService.getEquipmentById(1L);

        assertNotNull(foundEquipment);
        assertEquals("Laptop", foundEquipment.getType());
        verify(equipmentRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateEquipment() throws IOException {
        when(equipmentRepository.findByIdAndUserIdNumber(1L, "12345")).thenReturn(Optional.of(equipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(file.getOriginalFilename()).thenReturn("updated.pdf");
        when(file.getContentType()).thenReturn("application/pdf");

        Equipment updatedEquipment = equipmentService.updateEquipment(1L, "12345", "Tablet", "Silver", file);

        assertNotNull(updatedEquipment);
        assertEquals("Tablet", updatedEquipment.getType());
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
    }

    @Test
    void testDeleteEquipment() {
        when(equipmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(equipmentRepository).deleteById(1L);

        equipmentService.deleteEquipment(1L);

        verify(equipmentRepository, times(1)).deleteById(1L);
    }
}




//package com.fifgroup.fifpractice.service;
//
//import com.fifgroup.fifpractice.model.Equipment;
//import com.fifgroup.fifpractice.repository.EquipmentRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class EquipmentServiceTest {
//    @Mock
//    private EquipmentRepository equipmentRepository;
//
//    @InjectMocks
//    private EquipmentService equipmentService;
//
//    @Test
//    void testGetEquipmentById() {
//        Equipment equipment = new Equipment();
//        equipment.setId(1223423L);
//        equipment.setType("Laptop");
//        equipment.setColor("Black");
//        when(equipmentRepository.findById(1223423L)).thenReturn(Optional.of(equipment));
//
//        Equipment found = equipmentService.getEquipmentById(1223423L);
//        assertNotNull(found);
//        assertEquals("Laptop", found.getType());
//    }
//
//    @Test
//    void testSaveEquipment() {
//        Equipment equipment = new Equipment();
//        equipment.setId(1223423L);
//        equipment.setType("Mouse");
//        equipment.setColor("Red");
//        when(equipmentRepository.save(equipment)).thenReturn(equipment);
//
//        Equipment saved = equipmentService.saveEquipment(equipment);
//        assertEquals("Mouse", saved.getType());
//        assertEquals("Red", saved.getColor());
//    }
//}
