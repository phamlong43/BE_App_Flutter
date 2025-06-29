package com.example.app.controller;

import com.example.app.entity.OvertimeRecord;
import com.example.app.repository.OvertimeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/overtime-records")
public class OvertimeRecordController {
    @Autowired
    private OvertimeRecordRepository overtimeRecordRepository;

    @GetMapping
    public List<OvertimeRecord> getAll() {
        return overtimeRecordRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OvertimeRecord> getById(@PathVariable Integer id) {
        Optional<OvertimeRecord> record = overtimeRecordRepository.findById(id);
        return record.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public OvertimeRecord create(@RequestBody OvertimeRecord record) {
        return overtimeRecordRepository.save(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OvertimeRecord> update(@PathVariable Integer id, @RequestBody OvertimeRecord recordDetails) {
        Optional<OvertimeRecord> optionalRecord = overtimeRecordRepository.findById(id);
        if (!optionalRecord.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        OvertimeRecord record = optionalRecord.get();
        record.setEmployeeId(recordDetails.getEmployeeId());
        record.setDate(recordDetails.getDate());
        record.setHours(recordDetails.getHours());
        record.setReason(recordDetails.getReason());
        record.setApprovedBy(recordDetails.getApprovedBy());
        return ResponseEntity.ok(overtimeRecordRepository.save(record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!overtimeRecordRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        overtimeRecordRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

