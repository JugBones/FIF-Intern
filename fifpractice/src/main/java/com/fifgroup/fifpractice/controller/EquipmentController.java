package com.fifgroup.fifpractice.controller;

import com.fifgroup.fifpractice.model.Equipment;
import com.fifgroup.fifpractice.model.User;
import com.fifgroup.fifpractice.service.EquipmentService;
import com.fifgroup.fifpractice.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/equipment")
public class EquipmentController {
    private static final Logger logger = LogManager.getLogger(EquipmentController.class);

    private final EquipmentService equipmentService;
    private final UserService userService;

    public EquipmentController(EquipmentService equipmentService, UserService userService) {
        this.equipmentService = equipmentService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Equipment> uploadEquipment(
            @PathVariable String userId,
            @RequestParam("type") String type,
            @RequestParam("color") String color,
            @RequestParam("file") MultipartFile file) {

        logger.info("Uploading new equipment for user ID: {}", userId);
        Optional<User> user = userService.getUserById(userId);

        if (user.isPresent()) {
            Equipment equipment = new Equipment();
            equipment.setType(type);
            equipment.setColor(color);
            equipment.setUser(user.get());

            Equipment savedEquipment = equipmentService.saveEquipment(equipment, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipment);
        } else {
            logger.error("There is no User with ID {}", userId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipment(@PathVariable String userId) {
        logger.info("Fetching all equipment for user ID: {}", userId);

        List<Equipment> equipmentList = equipmentService.getEquipments(userId);

        if (equipmentList.isEmpty()) {
            logger.warn("No equipment found for user ID: {}", userId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(equipmentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipmentById(@PathVariable String userId, @PathVariable Long id) {
        logger.info("Fetching equipment ID: {} for user ID: {}", id, userId);

        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Equipment equipment = equipmentService.getEquipmentById(id);

            if (equipment != null && equipment.getUser().getIdNumber().equals(userId)) {
                return ResponseEntity.ok(equipment);
            } else {
                logger.warn("Equipment ID: {} not found or does not belong to user ID: {}", id, userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Equipment not found");
            }
        } else {
            logger.error("User ID {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String userId, @PathVariable Long id) {
        logger.info("Downloading file for equipment ID: {} under user ID: {}", id, userId);
        Equipment equipment = equipmentService.getEquipmentById(id);

        if (equipment != null && equipment.getUser().getIdNumber().equals(userId) && equipment.getFileData() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(equipment.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + equipment.getFileName() + "\"")
                    .body(equipment.getFileData());
        }

        logger.warn("No file found for equipment ID: {}", id);
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Equipment> updateEquipment(
            @PathVariable String userId,
            @PathVariable Long id,
            @RequestParam("type") String type,
            @RequestParam("color") String color,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        logger.info("Updating equipment ID: {} for user ID: {}", id, userId);

        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            logger.error("User with ID : {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Equipment updatedEquipment = equipmentService.updateEquipment(id, userId, type, color, file);

        if (updatedEquipment == null) {
            logger.warn("Equipment ID {} not found or does not belong to user ID: {}", id, userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info("Successfully updated equipment ID: {} for user ID: {}", id, userId);
        return ResponseEntity.ok(updatedEquipment);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable String userId, @PathVariable Long id) {
        logger.warn("Attempting to delete equipment ID: {} for user ID: {}", id, userId);

        Optional<User> user = userService.getUserById(userId);

        if (user.isEmpty()) {
            logger.error("User with ID {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Equipment equipment = equipmentService.getEquipmentById(id);
        if (equipment == null) {
            logger.error("Equipment ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!equipment.getUser().getIdNumber().equals(userId)) {
            logger.warn("Equipment ID {} does not belong to user ID {}", id, userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        equipmentService.deleteEquipment(id);
        logger.info("Successfully deleted equipment ID: {} for user ID: {}", id, userId);
        return ResponseEntity.noContent().build();
    }
}
