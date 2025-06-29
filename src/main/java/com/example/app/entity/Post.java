package com.example.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String author;

    private LocalDateTime createdAt;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer likes = 0;

    private Integer commentsCount = 0;

    @Convert(converter = VisibilityConverter.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility = Visibility.INTERNAL;

    private Integer shared = 0;

    public enum Visibility {
        PUBLIC, INTERNAL, PRIVATE;

        @com.fasterxml.jackson.annotation.JsonCreator
        public static Visibility fromString(String value) {
            for (Visibility v : Visibility.values()) {
                if (v.name().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Unknown enum value: " + value);
        }
    }

    // Getters and setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Integer getShared() {
        return shared;
    }

    public void setShared(Integer shared) {
        this.shared = shared;
    }

    @PrePersist
    public void prePersist() {
        if (visibility != null) {
            visibility = Visibility.valueOf(visibility.name().toUpperCase());
        }
    }
}
