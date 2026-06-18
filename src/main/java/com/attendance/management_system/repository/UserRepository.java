package com.attendance.management_system.repository;



import com.attendance.management_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Login ke waqt check karne ke liye custom function
    Optional<User> findByUsername(String username);
}