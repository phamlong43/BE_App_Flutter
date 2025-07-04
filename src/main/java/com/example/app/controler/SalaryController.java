package com.example.app.controler;

import com.example.app.entity.Salary;
import com.example.app.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {
    @Autowired
    private SalaryRepository salaryRepository;

    @GetMapping
    public List<Salary> getAll() {
        return salaryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return salaryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSalary(@RequestBody Salary salary) {
        if (salary.getUserId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }
        salary.setTotalSalary(calcTotalSalary(salary));
        Salary saved = salaryRepository.save(salary);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSalary(@PathVariable Integer id, @RequestBody Salary salary) {
        return salaryRepository.findById(id).map(existing -> {
            salary.setId(id);
            salary.setUserId(existing.getUserId()); // Không cho phép đổi userId
            salary.setTotalSalary(calcTotalSalary(salary));
            Salary updated = salaryRepository.save(salary);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSalary(@PathVariable Integer id) {
        if (!salaryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        salaryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private BigDecimal calcTotalSalary(Salary salary) {
        BigDecimal basic = salary.getBasicSalary() != null ? salary.getBasicSalary() : BigDecimal.ZERO;
        BigDecimal allowance = salary.getAllowance() != null ? salary.getAllowance() : BigDecimal.ZERO;
        BigDecimal bonus = salary.getBonus() != null ? salary.getBonus() : BigDecimal.ZERO;
        BigDecimal overtime = salary.getOvertimeSalary() != null ? salary.getOvertimeSalary() : BigDecimal.ZERO;
        BigDecimal deduction = salary.getDeduction() != null ? salary.getDeduction() : BigDecimal.ZERO;
        BigDecimal hourlyRate = salary.getHourlyRate() != null ? salary.getHourlyRate() : BigDecimal.ZERO;
        // If you want to use hourlyRate in totalSalary calculation, adjust here. Example:
        // return basic.add(allowance).add(bonus).add(overtime).add(hourlyRate).subtract(deduction);
        return basic.add(allowance).add(bonus).add(overtime).subtract(deduction);
    }
}
