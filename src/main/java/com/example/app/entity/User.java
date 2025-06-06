package com.example.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "employee_code", length = 50, unique = true)
    private String employeeCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('Male', 'Female', 'Other') DEFAULT 'Other'")
    private Gender gender = Gender.Other;

    @Column(name = "date_of_birth")
    private java.sql.Date dateOfBirth;

    @Column(length = 255)
    private String address;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt = new java.util.Date();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt = new java.util.Date();

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "place_of_birth", length = 255)
    private String placeOfBirth;

    @Column(name = "id_number", length = 50)
    private String idNumber;

    @Column(name = "id_issued_place", length = 255)
    private String idIssuedPlace;

    @Column(name = "id_issued_date")
    private java.sql.Date idIssuedDate;

    @Column(name = "ethnicity", length = 50)
    private String ethnicity;

    @Column(name = "religion", length = 50)
    private String religion;

    @Column(name = "nationality", length = 50)
    private String nationality;

    @Column(name = "marital_status", length = 50)
    private String maritalStatus;

    @Column(name = "education", length = 100)
    private String education;

    @Column(name = "permanent_address", length = 255)
    private String permanentAddress;

    @Column(name = "temporary_address", length = 255)
    private String temporaryAddress;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "work_status", length = 50)
    private String workStatus;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = new java.util.Date();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new java.util.Date();
    }

    public enum Gender {
        Male, Female, Other
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getFullName() { return fullName == null ? "" : fullName; }
    public void setFullName(String fullName) { this.fullName = (fullName == null ? "" : fullName); }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public java.sql.Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(java.sql.Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public java.util.Date getCreatedAt() { return createdAt; }
    public java.util.Date getUpdatedAt() { return updatedAt; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public String getIdIssuedPlace() { return idIssuedPlace; }
    public void setIdIssuedPlace(String idIssuedPlace) { this.idIssuedPlace = idIssuedPlace; }
    public java.sql.Date getIdIssuedDate() { return idIssuedDate; }
    public void setIdIssuedDate(java.sql.Date idIssuedDate) { this.idIssuedDate = idIssuedDate; }
    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }
    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }
    public String getTemporaryAddress() { return temporaryAddress; }
    public void setTemporaryAddress(String temporaryAddress) { this.temporaryAddress = temporaryAddress; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getWorkStatus() { return workStatus; }
    public void setWorkStatus(String workStatus) { this.workStatus = workStatus; }

    public User() {
        this.fullName = "";
    }
}
