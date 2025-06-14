package com.example.app.repository;

import com.example.app.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    boolean existsByUserIdAndMonthYear(Long userId, String monthYear);
    Salary findByUserIdAndMonthYear(Long userId, String monthYear);
}

