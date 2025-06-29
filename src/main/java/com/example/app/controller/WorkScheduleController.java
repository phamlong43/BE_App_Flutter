package com.example.app.controller;

import com.example.app.entity.WorkSchedule;
import com.example.app.repository.WorkScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/workschedules")
public class WorkScheduleController {
    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @GetMapping
    public List<WorkSchedule> getAll(@RequestParam(required = false) Long employeeId,
                                     @RequestParam(required = false) Long createdBy) {
        if (employeeId != null) {
            return workScheduleRepository.findByEmployeeId(employeeId);
        }
        if (createdBy != null) {
            return workScheduleRepository.findByCreatedBy(createdBy);
        }
        return workScheduleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkSchedule> getById(@PathVariable Long id) {
        Optional<WorkSchedule> ws = workScheduleRepository.findById(id);
        return ws.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public WorkSchedule create(@RequestBody WorkSchedule workSchedule) {
        return workScheduleRepository.save(workSchedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkSchedule> update(@PathVariable Long id, @RequestBody WorkSchedule workSchedule) {
        Optional<WorkSchedule> optional = workScheduleRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        workSchedule.setId(id);
        return ResponseEntity.ok(workScheduleRepository.save(workSchedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!workScheduleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        workScheduleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
