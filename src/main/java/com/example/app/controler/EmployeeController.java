package com.example.app.controler;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        // TODO: Implement logic
        return ResponseEntity.ok("Get all employees");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        // TODO: Implement logic
        return ResponseEntity.ok("Get employee by id: " + id);
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Map<String, Object> employee) {
        // TODO: Implement logic
        return ResponseEntity.ok("Employee created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Map<String, Object> employee) {
        // TODO: Implement logic
        return ResponseEntity.ok("Employee updated: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        // TODO: Implement logic
        return ResponseEntity.ok("Employee deleted: " + id);
    }
}

