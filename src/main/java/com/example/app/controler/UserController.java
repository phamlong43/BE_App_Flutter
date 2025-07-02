package com.example.app.controler;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.UserRepository;
import com.example.app.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", user.getId());
            result.put("username", user.getUsername());
            result.put("email", user.getEmail());
            result.put("role", user.getRole());
            result.put("full_name", user.getFullName());
            result.put("employee_code", user.getEmployeeCode());
            result.put("gender", user.getGender() != null ? user.getGender().name() : null);
            result.put("date_of_birth", user.getDateOfBirth());
            result.put("address", user.getAddress());
            result.put("created_at", user.getCreatedAt());
            result.put("updated_at", user.getUpdatedAt());
            result.put("mobile", user.getMobile());
            result.put("phone", user.getPhone());
            result.put("place_of_birth", user.getPlaceOfBirth());
            result.put("id_number", user.getIdNumber());
            result.put("id_issued_place", user.getIdIssuedPlace());
            result.put("id_issued_date", user.getIdIssuedDate());
            result.put("ethnicity", user.getEthnicity());
            result.put("religion", user.getReligion());
            result.put("nationality", user.getNationality());
            result.put("marital_status", user.getMaritalStatus());
            result.put("education", user.getEducation());
            result.put("permanent_address", user.getPermanentAddress());
            result.put("temporary_address", user.getTemporaryAddress());
            result.put("department", user.getDepartment());
            result.put("position", user.getPosition());
            result.put("work_status", user.getWorkStatus());
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(404).body("User not found");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> userMap) {
        try {
            String username = (String) userMap.get("username");
            String password = (String) userMap.get("password");
            String email = (String) userMap.get("email");
            String role = (String) userMap.getOrDefault("role", "USER");
            String fullName = (String) userMap.get("full_name");
            if (fullName == null) fullName = "";
            String employeeCode = (String) userMap.get("employee_code");
            String genderStr = (String) userMap.getOrDefault("gender", "Other");
            String dobStr = (String) userMap.get("date_of_birth");
            String address = (String) userMap.getOrDefault("address", "");
            String mobile = (String) userMap.getOrDefault("mobile", "");
            String phone = (String) userMap.getOrDefault("phone", "");
            String placeOfBirth = (String) userMap.getOrDefault("place_of_birth", "");
            String idNumber = (String) userMap.getOrDefault("id_number", "");
            String idIssuedPlace = (String) userMap.getOrDefault("id_issued_place", "");
            String idIssuedDateStr = (String) userMap.get("id_issued_date");
            String ethnicity = (String) userMap.getOrDefault("ethnicity", "");
            String religion = (String) userMap.getOrDefault("religion", "");
            String nationality = (String) userMap.getOrDefault("nationality", "");
            String maritalStatus = (String) userMap.getOrDefault("marital_status", "");
            String education = (String) userMap.getOrDefault("education", "");
            String permanentAddress = (String) userMap.getOrDefault("permanent_address", "");
            String temporaryAddress = (String) userMap.getOrDefault("temporary_address", "");
            String department = (String) userMap.getOrDefault("department", "");
            String position = (String) userMap.getOrDefault("position", "");
            String workStatus = (String) userMap.getOrDefault("work_status", "");
            if (username == null || password == null || email == null) {
                return ResponseEntity.badRequest().body("Missing required fields: username, password, email");
            }
            if (userRepository.findByUsername(username).isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
            if (userRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("Email already exists");
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setRole(role);
            user.setFullName(fullName);
            user.setEmployeeCode(employeeCode);
            try {
                user.setGender(User.Gender.valueOf(genderStr));
            } catch (Exception e) {
                user.setGender(User.Gender.Other);
            }
            if (dobStr != null && !dobStr.isEmpty()) {
                user.setDateOfBirth(java.sql.Date.valueOf(dobStr));
            }
            user.setAddress(address);
            user.setMobile(mobile);
            user.setPhone(phone);
            user.setPlaceOfBirth(placeOfBirth);
            user.setIdNumber(idNumber);
            user.setIdIssuedPlace(idIssuedPlace);
            if (idIssuedDateStr != null && !idIssuedDateStr.isEmpty()) {
                user.setIdIssuedDate(java.sql.Date.valueOf(idIssuedDateStr));
            }
            user.setEthnicity(ethnicity);
            user.setReligion(religion);
            user.setNationality(nationality);
            user.setMaritalStatus(maritalStatus);
            user.setEducation(education);
            user.setPermanentAddress(permanentAddress);
            user.setTemporaryAddress(temporaryAddress);
            user.setDepartment(department);
            user.setPosition(position);
            user.setWorkStatus(workStatus);
            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> credentials) {
        String username = (String) credentials.get("username");
        String password = (String) credentials.get("password");
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                Map<String, Object> result = new java.util.HashMap<>();
                result.put("id", user.getId());
                result.put("username", user.getUsername());
                result.put("email", user.getEmail());
                result.put("role", user.getRole());
                return ResponseEntity.ok(result);
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userMap) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        User user = userOpt.get();
        if (userMap.containsKey("username")) {
            String newUsername = (String) userMap.get("username");
            if (!newUsername.equals(user.getUsername()) && userRepository.findByUsername(newUsername).isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
            user.setUsername(newUsername);
        }
        if (userMap.containsKey("email")) {
            String newEmail = (String) userMap.get("email");
            if (!newEmail.equals(user.getEmail()) && userRepository.findByEmail(newEmail).isPresent()) {
                return ResponseEntity.badRequest().body("Email already exists");
            }
            user.setEmail(newEmail);
        }
        if (userMap.containsKey("password")) {
            String newPassword = (String) userMap.get("password");
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        if (userMap.containsKey("role")) {
            user.setRole((String) userMap.get("role"));
        }
        if (userMap.containsKey("full_name")) {
            user.setFullName((String) userMap.get("full_name"));
        }
        if (userMap.containsKey("employee_code")) {
            user.setEmployeeCode((String) userMap.get("employee_code"));
        }
        if (userMap.containsKey("gender")) {
            try {
                user.setGender(User.Gender.valueOf((String) userMap.get("gender")));
            } catch (Exception e) {
                user.setGender(User.Gender.Other);
            }
        }
        if (userMap.containsKey("date_of_birth")) {
            String dobStr = (String) userMap.get("date_of_birth");
            if (dobStr != null && !dobStr.isEmpty()) {
                user.setDateOfBirth(java.sql.Date.valueOf(dobStr));
            }
        }
        if (userMap.containsKey("address")) {
            user.setAddress((String) userMap.get("address"));
        }
        if (userMap.containsKey("mobile")) {
            user.setMobile((String) userMap.get("mobile"));
        }
        if (userMap.containsKey("phone")) {
            user.setPhone((String) userMap.get("phone"));
        }
        if (userMap.containsKey("place_of_birth")) {
            user.setPlaceOfBirth((String) userMap.get("place_of_birth"));
        }
        if (userMap.containsKey("id_number")) {
            user.setIdNumber((String) userMap.get("id_number"));
        }
        if (userMap.containsKey("id_issued_place")) {
            user.setIdIssuedPlace((String) userMap.get("id_issued_place"));
        }
        if (userMap.containsKey("id_issued_date")) {
            String idIssuedDateStr = (String) userMap.get("id_issued_date");
            if (idIssuedDateStr != null && !idIssuedDateStr.isEmpty()) {
                user.setIdIssuedDate(java.sql.Date.valueOf(idIssuedDateStr));
            }
        }
        if (userMap.containsKey("ethnicity")) {
            user.setEthnicity((String) userMap.get("ethnicity"));
        }
        if (userMap.containsKey("religion")) {
            user.setReligion((String) userMap.get("religion"));
        }
        if (userMap.containsKey("nationality")) {
            user.setNationality((String) userMap.get("nationality"));
        }
        if (userMap.containsKey("marital_status")) {
            user.setMaritalStatus((String) userMap.get("marital_status"));
        }
        if (userMap.containsKey("education")) {
            user.setEducation((String) userMap.get("education"));
        }
        if (userMap.containsKey("permanent_address")) {
            user.setPermanentAddress((String) userMap.get("permanent_address"));
        }
        if (userMap.containsKey("temporary_address")) {
            user.setTemporaryAddress((String) userMap.get("temporary_address"));
        }
        if (userMap.containsKey("department")) {
            user.setDepartment((String) userMap.get("department"));
        }
        if (userMap.containsKey("position")) {
            user.setPosition((String) userMap.get("position"));
        }
        if (userMap.containsKey("work_status")) {
            user.setWorkStatus((String) userMap.get("work_status"));
        }
        userRepository.save(user);
        return ResponseEntity.ok("User updated successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");
        if (username == null || oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping({"", "/"})
    public ResponseEntity<?> getAllUsers() {
        var users = userRepository.findAll();
        var result = new java.util.ArrayList<Map<String, Object>>();
        for (User user : users) {
            Map<String, Object> item = new java.util.HashMap<>();
            item.put("id", user.getId());
            item.put("username", user.getUsername());
            item.put("email", user.getEmail());
            item.put("role", user.getRole());
            item.put("full_name", user.getFullName());
            item.put("employee_code", user.getEmployeeCode());
            item.put("gender", user.getGender() != null ? user.getGender().name() : null);
            item.put("date_of_birth", user.getDateOfBirth());
            item.put("address", user.getAddress());
            item.put("created_at", user.getCreatedAt());
            item.put("updated_at", user.getUpdatedAt());
            item.put("mobile", user.getMobile());
            item.put("phone", user.getPhone());
            item.put("place_of_birth", user.getPlaceOfBirth());
            item.put("id_number", user.getIdNumber());
            item.put("id_issued_place", user.getIdIssuedPlace());
            item.put("id_issued_date", user.getIdIssuedDate());
            item.put("ethnicity", user.getEthnicity());
            item.put("religion", user.getReligion());
            item.put("nationality", user.getNationality());
            item.put("marital_status", user.getMaritalStatus());
            item.put("education", user.getEducation());
            item.put("permanent_address", user.getPermanentAddress());
            item.put("temporary_address", user.getTemporaryAddress());
            item.put("department", user.getDepartment());
            item.put("position", user.getPosition());
            item.put("work_status", user.getWorkStatus());
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", user.getId());
            result.put("username", user.getUsername());
            result.put("email", user.getEmail());
            result.put("role", user.getRole());
            result.put("full_name", user.getFullName());
            result.put("employee_code", user.getEmployeeCode());
            result.put("gender", user.getGender() != null ? user.getGender().name() : null);
            result.put("date_of_birth", user.getDateOfBirth());
            result.put("address", user.getAddress());
            result.put("created_at", user.getCreatedAt());
            result.put("updated_at", user.getUpdatedAt());
            result.put("mobile", user.getMobile());
            result.put("phone", user.getPhone());
            result.put("place_of_birth", user.getPlaceOfBirth());
            result.put("id_number", user.getIdNumber());
            result.put("id_issued_place", user.getIdIssuedPlace());
            result.put("id_issued_date", user.getIdIssuedDate());
            result.put("ethnicity", user.getEthnicity());
            result.put("religion", user.getReligion());
            result.put("nationality", user.getNationality());
            result.put("marital_status", user.getMaritalStatus());
            result.put("education", user.getEducation());
            result.put("permanent_address", user.getPermanentAddress());
            result.put("temporary_address", user.getTemporaryAddress());
            result.put("department", user.getDepartment());
            result.put("position", user.getPosition());
            result.put("work_status", user.getWorkStatus());
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(404).body("User not found");
    }
}
