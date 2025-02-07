package com.fifgroup.fifpractice.service;

import com.fifgroup.fifpractice.model.Equipment;
import com.fifgroup.fifpractice.repository.EquipmentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {
    private static final Logger logger = LogManager.getLogger(EquipmentService.class);
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public Equipment saveEquipment(Equipment equipment, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                equipment.setFileData(file.getBytes());
                equipment.setFileName(file.getOriginalFilename());
                equipment.setFileType(file.getContentType());
            }
            Equipment savedEquipment = equipmentRepository.save(equipment);
            logger.info("Successfully saved equipment with ID: {}", savedEquipment.getId());
            return savedEquipment;
        } catch (IOException e) {
            logger.error("Failed to process file for equipment ID: {}", equipment.getId(), e);
            throw new RuntimeException("Error processing file", e);
        }
    }

    public List<Equipment> getEquipments(String userId) {
        logger.info("Fetching all equipment for user ID: {}", userId);
        return equipmentRepository.findByUserIdNumber(userId);
    }

    public Equipment getEquipmentById(Long id) {
        logger.info("Fetching equipment with ID: {}", id);
        return equipmentRepository.findById(id).orElse(null);
    }

    public Equipment updateEquipment(Long id, String userId, String type, String color, MultipartFile file) {
        logger.info("Attempting to update equipment ID: {} for user ID: {}", id, userId);

        Equipment equipment = equipmentRepository.findByIdAndUserIdNumber(id, userId).orElse(null);
        if (equipment == null) {
            logger.warn("Equipment ID {} not found or does not belong to user ID: {}", id, userId);
            return null;
        }

        equipment.setType(type);
        equipment.setColor(color);

        // Update file if provided
        if (file != null && !file.isEmpty()) {
            try {
                equipment.setFileData(file.getBytes());
                equipment.setFileName(file.getOriginalFilename());
                equipment.setFileType(file.getContentType());
                logger.info("Updated file for equipment ID: {}", id);
            } catch (IOException e) {
                logger.error("Failed to process file for equipment ID: {}", id, e);
                throw new RuntimeException("Error processing file", e);
            }
        }

        Equipment savedEquipment = equipmentRepository.save(equipment);
        logger.info("Successfully updated equipment ID: {}", id);
        return savedEquipment;
    }


    public void deleteEquipment(Long id) {
        if (equipmentRepository.existsById(id)) {
            equipmentRepository.deleteById(id);
            logger.info("Deleted equipment with ID: {}", id);
        } else {
            logger.warn("Attempted to delete non-existent equipment ID: {}", id);
        }
    }
}
