package com.example.app.repository;

import com.example.app.entity.FaceRegisterRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FaceRegisterRequestRepository extends JpaRepository<FaceRegisterRequest, Long> {
    Optional<FaceRegisterRequest> findByUser_Id(Long userId);
}
