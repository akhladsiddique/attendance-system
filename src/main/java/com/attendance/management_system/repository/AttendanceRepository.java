package com.attendance.management_system.repository;

import com.attendance.management_system.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUserId(Long userId);

    // Optional ki jagah List use karenge taaki agar pehle se multiple records hon toh crash na ho
    List<Attendance> findByUserIdAndDate(Long userId, LocalDate date);
}