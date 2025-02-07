package com.fifgroup.fifpractice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    private String idNumber;

    private String name;
    private String address;
    private LocalDate birthDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Equipment> equipmentList;
}
