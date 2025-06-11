package com.example.app.controler;

import com.example.app.entity.Request;
import com.example.app.entity.User;
import com.example.app.repository.RequestRepository;
import com.example.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/requests")
public class RequestController {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    // User gửi yêu cầu
    @PostMapping("")
    public ResponseEntity<?> createRequest(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String requestTypeStr = payload.get("requestType");
        String title = payload.get("title");
        String description = payload.get("description");
        String startDateStr = payload.get("startDate");
        String endDateStr = payload.get("endDate");
        String reason = payload.get("reason");
        if (username == null || requestTypeStr == null || title == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Request req = new Request();
        req.setUser(userOpt.get());
        try {
            req.setRequestType(Request.RequestType.valueOf(requestTypeStr));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid requestType. Allowed: leave, overtime, equipment, other");
        }
        req.setTitle(title);
        req.setDescription(description);
        req.setReason(reason);
        req.setStatus(Request.Status.pending);
        req.setCreatedAt(LocalDateTime.now());
        req.setUpdatedAt(LocalDateTime.now());
        try {
            if (startDateStr != null && !startDateStr.isEmpty()) req.setStartDate(java.sql.Date.valueOf(startDateStr));
            if (endDateStr != null && !endDateStr.isEmpty()) req.setEndDate(java.sql.Date.valueOf(endDateStr));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format, use yyyy-MM-dd");
        }
        requestRepository.save(req);
        return ResponseEntity.ok("Request submitted successfully");
    }

    // User xem yêu cầu của mình
    @GetMapping("/my")
    public ResponseEntity<?> getMyRequests(@RequestParam String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        List<Request> requests = requestRepository.findByUser(userOpt.get());
        List<Map<String, Object>> result = new ArrayList<>();
        for (Request req : requests) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", req.getId());
            item.put("requestType", req.getRequestType());
            item.put("title", req.getTitle());
            item.put("description", req.getDescription());
            item.put("startDate", req.getStartDate());
            item.put("endDate", req.getEndDate());
            item.put("reason", req.getReason());
            item.put("status", req.getStatus());
            item.put("createdAt", req.getCreatedAt());
            item.put("updatedAt", req.getUpdatedAt());
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    // Admin xem tất cả yêu cầu
    @GetMapping("/all")
    public ResponseEntity<?> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Request req : requests) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", req.getId());
            item.put("username", req.getUser().getUsername());
            item.put("requestType", req.getRequestType());
            item.put("title", req.getTitle());
            item.put("description", req.getDescription());
            item.put("startDate", req.getStartDate());
            item.put("endDate", req.getEndDate());
            item.put("reason", req.getReason());
            item.put("status", req.getStatus());
            item.put("createdAt", req.getCreatedAt());
            item.put("updatedAt", req.getUpdatedAt());
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    // Admin duyệt yêu cầu
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        Optional<Request> reqOpt = requestRepository.findById(id);
        if (reqOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Request not found");
        }
        Request req = reqOpt.get();
        req.setStatus(Request.Status.approved);
        req.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(req);
        return ResponseEntity.ok("Request approved");
    }

    // Admin từ chối yêu cầu
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
        Optional<Request> reqOpt = requestRepository.findById(id);
        if (reqOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Request not found");
        }
        Request req = reqOpt.get();
        req.setStatus(Request.Status.rejected);
        req.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(req);
        return ResponseEntity.ok("Request rejected");
    }
}
