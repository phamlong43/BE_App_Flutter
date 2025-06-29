package com.example.app.controller;

import com.example.app.entity.Document;
import com.example.app.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
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

