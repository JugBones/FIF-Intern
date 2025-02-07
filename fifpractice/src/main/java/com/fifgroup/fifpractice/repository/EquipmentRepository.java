package com.fifgroup.fifpractice.repository;

import com.fifgroup.fifpractice.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByUserIdNumber(String userId);
    Optional<Equipment> findByIdAndUserIdNumber(Long id, String userId);
}
