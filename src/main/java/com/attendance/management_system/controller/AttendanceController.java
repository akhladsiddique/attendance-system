package com.attendance.management_system.controller;

import com.attendance.management_system.model.Attendance;
import com.attendance.management_system.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping("/punch-in")
    public ResponseEntity<?> punchIn(@RequestBody Attendance attendance) {
        try {
            Long userId = attendance.getUser().getId();
            LocalDate today = LocalDate.now();

            // Sahi Repository matching: List use ho rahi hai bina kisi mismatch ke
            List<Attendance> existingLogs = attendanceRepository.findByUserIdAndDate(userId, today);

            if (existingLogs != null && !existingLogs.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "You have already punched in for today!"));
            }

            attendance.setDate(today);
            attendance.setPunchInTime(LocalTime.now());
            attendance.setStatus("PRESENT");

            Attendance savedAttendance = attendanceRepository.save(attendance);
            return ResponseEntity.ok(savedAttendance);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Server processing fault: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public List<Attendance> getAttendanceByUserId(@PathVariable Long userId) {
        return attendanceRepository.findByUserId(userId);
    }

    @PutMapping("/punch-out/{id}")
    public ResponseEntity<String> punchOut(@PathVariable Long id) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(id);
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            attendance.setPunchOutTime(LocalTime.now());
            attendanceRepository.save(attendance);
            return ResponseEntity.ok("Punch-Out Successful! Time: " + attendance.getPunchOutTime());
        }
        return ResponseEntity.status(404).body("Attendance session record not found.");
    }
}