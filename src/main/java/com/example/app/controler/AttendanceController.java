package com.example.app.controler;

import com.example.app.entity.Attendance;
import com.example.app.entity.User;
import com.example.app.repository.AttendanceRepository;
import com.example.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createAttendance(@RequestBody Attendance attendance) {
        if (attendance.getUser() == null || attendance.getUser().getId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }
        Optional<User> userOpt = userRepository.findById(attendance.getUser().getId());
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        attendance.setUser(userOpt.get());
        Attendance saved = attendanceRepository.save(attendance);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Attendance> getAll() {
        return attendanceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return attendanceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        return attendanceRepository.findById(id).map(existing -> {
            if (attendance.getCheckOut() != null) {
                existing.setCheckOut(attendance.getCheckOut());
            }
            if (attendance.getStatus() != null) {
                existing.setStatus(attendance.getStatus());
            }
            // Có thể bổ sung các trường khác nếu cần
            Attendance updated = attendanceRepository.save(existing);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long id) {
        if (!attendanceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        attendanceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public List<Attendance> getByUserId(@PathVariable Long userId) {
        return attendanceRepository.findByUser_Id(userId);
    }
}
