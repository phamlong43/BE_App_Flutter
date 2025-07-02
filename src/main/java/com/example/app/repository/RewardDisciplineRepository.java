package com.example.app.repository;

import com.example.app.entity.RewardDiscipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardDisciplineRepository extends JpaRepository<RewardDiscipline, Long> {
    List<RewardDiscipline> findByEmployeeId(Long employeeId);
}
