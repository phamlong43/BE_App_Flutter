package com.example.app.controller;

import com.example.app.entity.FeedbackBox;
import com.example.app.repository.FeedbackBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedback-box")
public class FeedbackBoxController {
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

