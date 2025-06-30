package com.example.app.controler;

import com.example.app.entity.*;
import com.example.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    // API cho admin giao nhiệm vụ cho user
    @PostMapping("")
    public ResponseEntity<?> createTask(@RequestBody Map<String, Object> payload) {
        Object usernamesObj = payload.get("usernames");
        String taskName = (String) payload.get("taskName");
        String description = (String) payload.get("description");
        String dueDateStr = (String) payload.get("dueDate");
        if (usernamesObj == null || taskName == null) {
            return ResponseEntity.badRequest().body("Missing usernames or taskName");
        }
        List<String> usernames;
        if (usernamesObj instanceof List) {
            usernames = (List<String>) usernamesObj;
        } else if (usernamesObj instanceof String) {
            usernames = List.of((String)usernamesObj);
        } else {
            return ResponseEntity.badRequest().body("Invalid usernames format");
        }
        List<String> notFound = new ArrayList<>();
        int created = 0;
        for (String username : usernames) {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                notFound.add(username);
                continue;
            }
            Task task = new Task();
            task.setUser(userOpt.get());
            task.setTaskName(taskName);
            task.setDescription(description);
            task.setStatus("pending");
            task.setAssignedAt(java.time.LocalDateTime.now());
            if (dueDateStr != null && !dueDateStr.isEmpty()) {
                try {
                    task.setDueDate(java.sql.Date.valueOf(dueDateStr));
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Invalid dueDate format, use yyyy-MM-dd");
                }
            }
            taskRepository.save(task);
            created++;
        }
        if (notFound.isEmpty()) {
            return ResponseEntity.ok("Task assigned to " + created + " user(s) successfully");
        } else {
            return ResponseEntity.ok("Task assigned to " + created + " user(s). Not found: " + notFound);
        }
    }

    // API cho user xem nhiệm vụ của mình
    @GetMapping("/my")
    public ResponseEntity<?> getMyTasks(@RequestParam String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        List<Task> tasks = taskRepository.findByUser(userOpt.get());
        List<Map<String, Object>> result = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", task.getId());
            item.put("taskName", task.getTaskName());
            item.put("description", task.getDescription());
            item.put("status", task.getStatus());
            item.put("assignedAt", task.getAssignedAt());
            item.put("dueDate", task.getDueDate());
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    // API cho admin xem toàn bộ nhiệm vụ đã giao
    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", task.getId());
            item.put("taskName", task.getTaskName());
            item.put("description", task.getDescription());
            item.put("status", task.getStatus());
            item.put("assignedAt", task.getAssignedAt());
            item.put("dueDate", task.getDueDate());
            item.put("username", task.getUser().getUsername());
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    // API cho user cập nhật trạng thái task (hoàn thành hoặc không thể hoàn thành)
    @PostMapping("/update-status")
    public ResponseEntity<?> updateTaskStatus(@RequestBody Map<String, Object> payload) {
        Long taskId;
        String status;
        String username;
        try {
            taskId = Long.valueOf(payload.get("taskId").toString());
            status = payload.get("status").toString();
            username = payload.get("username").toString();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Missing or invalid taskId, status, or username");
        }
        if (!status.equalsIgnoreCase("completed") && !status.equalsIgnoreCase("failed")) {
            return ResponseEntity.badRequest().body("Status must be 'completed' or 'failed'");
        }
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Task not found");
        }
        Task task = taskOpt.get();
        if (!task.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(403).body("You do not have permission to update this task");
        }
        task.setStatus(status.toLowerCase());
        taskRepository.save(task);
        return ResponseEntity.ok("Task status updated successfully");
    }

    @RestController
    @RequestMapping("/api/documents")
    public static class DocumentController {
        @Autowired
        private DocumentRepository documentRepository;

        @GetMapping
        public List<Document> getAll() {
            return documentRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Document> getById(@PathVariable Integer id) {
            Optional<Document> doc = documentRepository.findById(id);
            return doc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public Document create(@RequestBody Document document) {
            return documentRepository.save(document);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Document> update(@PathVariable Integer id, @RequestBody Document documentDetails) {
            Optional<Document> optionalDoc = documentRepository.findById(id);
            if (!optionalDoc.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            Document doc = optionalDoc.get();
            doc.setTitle(documentDetails.getTitle());
            doc.setDescription(documentDetails.getDescription());
            doc.setFilePath(documentDetails.getFilePath());
            doc.setUploadedBy(documentDetails.getUploadedBy());
            doc.setUploadedAt(documentDetails.getUploadedAt());
            doc.setCategory(documentDetails.getCategory());
            return ResponseEntity.ok(documentRepository.save(doc));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Integer id) {
            if (!documentRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            documentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

    @RestController
    @RequestMapping("/api/feedback-box")
    public static class FeedbackBoxController {
        @Autowired
        private FeedbackBoxRepository feedbackBoxRepository;

        @GetMapping
        public List<FeedbackBox> getAll() {
            return feedbackBoxRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<FeedbackBox> getById(@PathVariable Integer id) {
            Optional<FeedbackBox> feedback = feedbackBoxRepository.findById(id);
            return feedback.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public FeedbackBox create(@RequestBody FeedbackBox feedbackBox) {
            return feedbackBoxRepository.save(feedbackBox);
        }

        @PutMapping("/{id}")
        public ResponseEntity<FeedbackBox> update(@PathVariable Integer id, @RequestBody FeedbackBox feedbackDetails) {
            Optional<FeedbackBox> optionalFeedback = feedbackBoxRepository.findById(id);
            if (!optionalFeedback.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            FeedbackBox feedback = optionalFeedback.get();
            feedback.setSenderName(feedbackDetails.getSenderName());
            feedback.setContent(feedbackDetails.getContent());
            feedback.setCreatedAt(feedbackDetails.getCreatedAt());
            feedback.setStatus(feedbackDetails.getStatus());
            return ResponseEntity.ok(feedbackBoxRepository.save(feedback));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Integer id) {
            if (!feedbackBoxRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            feedbackBoxRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

    @RestController
    @RequestMapping("/api/overtime-records")
    public static class OvertimeRecordController {
        @Autowired
        private OvertimeRecordRepository overtimeRecordRepository;

        @GetMapping
        public List<OvertimeRecord> getAll() {
            return overtimeRecordRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<OvertimeRecord> getById(@PathVariable Integer id) {
            Optional<OvertimeRecord> record = overtimeRecordRepository.findById(id);
            return record.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public OvertimeRecord create(@RequestBody OvertimeRecord record) {
            return overtimeRecordRepository.save(record);
        }

        @PutMapping("/{id}")
        public ResponseEntity<OvertimeRecord> update(@PathVariable Integer id, @RequestBody OvertimeRecord recordDetails) {
            Optional<OvertimeRecord> optionalRecord = overtimeRecordRepository.findById(id);
            if (!optionalRecord.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            OvertimeRecord record = optionalRecord.get();
            record.setEmployeeId(recordDetails.getEmployeeId());
            record.setDate(recordDetails.getDate());
            record.setHours(recordDetails.getHours());
            record.setReason(recordDetails.getReason());
            record.setApprovedBy(recordDetails.getApprovedBy());
            return ResponseEntity.ok(overtimeRecordRepository.save(record));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Integer id) {
            if (!overtimeRecordRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            overtimeRecordRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

    // Đã xóa hoàn toàn inner static class ProjectController để tránh trùng lặp bean mapping

    @RestController
    @RequestMapping("/api/workschedules")
    public static class WorkScheduleController {
        @Autowired
        private WorkScheduleRepository workScheduleRepository;

        @GetMapping
        public List<WorkSchedule> getAll(@RequestParam(required = false) Long employeeId,
                                         @RequestParam(required = false) Long createdBy) {
            if (employeeId != null) {
                return workScheduleRepository.findByEmployeeId(employeeId);
            }
            if (createdBy != null) {
                return workScheduleRepository.findByCreatedBy(createdBy);
            }
            return workScheduleRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<WorkSchedule> getById(@PathVariable Long id) {
            Optional<WorkSchedule> ws = workScheduleRepository.findById(id);
            return ws.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public WorkSchedule create(@RequestBody WorkSchedule workSchedule) {
            return workScheduleRepository.save(workSchedule);
        }

        @PutMapping("/{id}")
        public ResponseEntity<WorkSchedule> update(@PathVariable Long id, @RequestBody WorkSchedule workSchedule) {
            Optional<WorkSchedule> optional = workScheduleRepository.findById(id);
            if (!optional.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            workSchedule.setId(id);
            return ResponseEntity.ok(workScheduleRepository.save(workSchedule));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            if (!workScheduleRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            workScheduleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}
