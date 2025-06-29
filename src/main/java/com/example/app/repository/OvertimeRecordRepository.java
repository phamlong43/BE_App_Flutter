package com.example.app.repository;

import com.example.app.entity.OvertimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OvertimeRecordRepository extends JpaRepository<OvertimeRecord, Integer> {
}

