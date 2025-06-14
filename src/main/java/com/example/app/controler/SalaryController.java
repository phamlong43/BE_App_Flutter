package com.example.app.controler;

import com.example.app.entity.Salary;
import com.example.app.entity.User;
import com.example.app.repository.SalaryRepository;
import com.example.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {
    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Salary> getAll() {
        return salaryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return salaryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSalary(@RequestBody Salary salary) {
        if (salary.getUser() == null || salary.getUser().getId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }
        Optional<User> userOpt = userRepository.findById(salary.getUser().getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        salary.setUser(userOpt.get());
        salary.setTotalSalary(calcTotalSalary(salary));
        Salary saved = salaryRepository.save(salary);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSalary(@PathVariable Long id, @RequestBody Salary salary) {
        return salaryRepository.findById(id).map(existing -> {
            salary.setId(id);
            salary.setUser(existing.getUser()); // Không cho phép đổi user
            salary.setTotalSalary(calcTotalSalary(salary));
            Salary updated = salaryRepository.save(salary);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSalary(@PathVariable Long id) {
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
        return basic.add(allowance).add(bonus).add(overtime).subtract(deduction);
    }
}

