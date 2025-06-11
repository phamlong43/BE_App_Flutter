package com.example.app.controler;

import com.example.app.entity.Task;
import com.example.app.entity.User;
import com.example.app.repository.TaskRepository;
import com.example.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
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
}
