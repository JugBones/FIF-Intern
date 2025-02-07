package com.fifgroup.fifpractice.repository;

import com.fifgroup.fifpractice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}