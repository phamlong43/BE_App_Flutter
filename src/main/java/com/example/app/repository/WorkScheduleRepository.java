package com.example.app.repository;

import com.example.app.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
    List<WorkSchedule> findByEmployeeId(Long employeeId);
    List<WorkSchedule> findByCreatedBy(Long createdBy);
}
