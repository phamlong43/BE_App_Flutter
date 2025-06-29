package com.example.app.controler;

import com.example.app.entity.RecruitmentPost;
import com.example.app.repository.RecruitmentPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recruitment-posts")
public class RecruitmentPostController {
    @Autowired
    private RecruitmentPostRepository recruitmentPostRepository;

    @GetMapping
    public List<RecruitmentPost> getAll() {
        return recruitmentPostRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentPost> getById(@PathVariable Integer id) {
        Optional<RecruitmentPost> post = recruitmentPostRepository.findById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public RecruitmentPost create(@RequestBody RecruitmentPost post) {
        return recruitmentPostRepository.save(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentPost> update(@PathVariable Integer id, @RequestBody RecruitmentPost postDetails) {
        Optional<RecruitmentPost> optionalPost = recruitmentPostRepository.findById(id);
        if (!optionalPost.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        RecruitmentPost post = optionalPost.get();
        post.setTitle(postDetails.getTitle());
        post.setDescription(postDetails.getDescription());
        post.setRequirements(postDetails.getRequirements());
        post.setPostedBy(postDetails.getPostedBy());
        post.setPostedDate(postDetails.getPostedDate());
        post.setDeadline(postDetails.getDeadline());
        post.setStatus(postDetails.getStatus());
        return ResponseEntity.ok(recruitmentPostRepository.save(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!recruitmentPostRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        recruitmentPostRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

