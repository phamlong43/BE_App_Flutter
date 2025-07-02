package com.example.app.controler;

import com.example.app.entity.RewardDiscipline;
import com.example.app.repository.RewardDisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reward-discipline")
public class RewardDisciplineController {
    @Autowired
    private RewardDisciplineRepository rewardDisciplineRepository;

    @GetMapping
    public List<RewardDiscipline> getAll() {
        return rewardDisciplineRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<RewardDiscipline> result = rewardDisciplineRepository.findById(id);
        if (result.isPresent()) {
            RewardDiscipline rd = result.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", rd.getId());
            response.put("employeeId", rd.getEmployeeId());
            response.put("type", rd.getType());
            response.put("title", rd.getTitle());
            response.put("reason", rd.getReason());
            response.put("amount", rd.getAmount());
            response.put("effectiveDate", rd.getEffectiveDate());
            response.put("notes", rd.getNotes());
            response.put("createdAt", rd.getCreatedAt());
            response.put("updatedAt", rd.getUpdatedAt());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RewardDiscipline rewardDiscipline) {
        rewardDiscipline.setCreatedAt(LocalDateTime.now());
        rewardDiscipline.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(rewardDisciplineRepository.save(rewardDiscipline));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RewardDiscipline rewardDiscipline) {
        Optional<RewardDiscipline> existing = rewardDisciplineRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        RewardDiscipline rd = existing.get();
        rd.setEmployeeId(rewardDiscipline.getEmployeeId());
        rd.setType(rewardDiscipline.getType());
        rd.setTitle(rewardDiscipline.getTitle());
        rd.setReason(rewardDiscipline.getReason());
        rd.setAmount(rewardDiscipline.getAmount());
        rd.setEffectiveDate(rewardDiscipline.getEffectiveDate());
        rd.setNotes(rewardDiscipline.getNotes());
        rd.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(rewardDisciplineRepository.save(rd));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!rewardDisciplineRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rewardDisciplineRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employee/{employeeId}")
    public List<RewardDiscipline> getByEmployeeId(@PathVariable Long employeeId) {
        return rewardDisciplineRepository.findByEmployeeId(employeeId);
    }
}
