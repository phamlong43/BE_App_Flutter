package com.example.app.controler;

import com.example.app.entity.FaceRegisterRequest;
import com.example.app.entity.User;
import com.example.app.repository.FaceRegisterRequestRepository;
import com.example.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/face-register-requests")
public class FaceRegisterRequestController {
    @Autowired
    private FaceRegisterRequestRepository faceRegisterRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestParam Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        // Kiểm tra user đã có yêu cầu đăng ký chưa
        if (faceRegisterRequestRepository.findByUser_Id(userId).isPresent()) {
            return ResponseEntity.badRequest().body("User đã có yêu cầu đăng ký xác thực!");
        }
        FaceRegisterRequest request = new FaceRegisterRequest();
        request.setUser(userOpt.get());
        request.setStatus(FaceRegisterRequest.Status.PENDING);
        FaceRegisterRequest saved = faceRegisterRequestRepository.save(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(faceRegisterRequestRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam FaceRegisterRequest.Status status) {
        Optional<FaceRegisterRequest> reqOpt = faceRegisterRequestRepository.findById(id);
        if (reqOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        FaceRegisterRequest req = reqOpt.get();
        req.setStatus(status);
        faceRegisterRequestRepository.save(req);
        return ResponseEntity.ok(req);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchFaceRegisterRequest(@PathVariable Long id, @RequestBody FaceRegisterRequest patch) {
        Optional<FaceRegisterRequest> reqOpt = faceRegisterRequestRepository.findById(id);
        if (reqOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        FaceRegisterRequest req = reqOpt.get();
        if (patch.getStatus() != null) {
            req.setStatus(patch.getStatus());
        }
        // Có thể bổ sung các trường khác nếu cần
        faceRegisterRequestRepository.save(req);
        return ResponseEntity.ok(req);
    }
}
