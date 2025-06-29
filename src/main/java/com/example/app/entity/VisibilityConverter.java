package com.example.app.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VisibilityConverter implements AttributeConverter<Post.Visibility, String> {
    @Override
    public String convertToDatabaseColumn(Post.Visibility attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public Post.Visibility convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (Post.Visibility v : Post.Visibility.values()) {
            if (v.name().equalsIgnoreCase(dbData)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + dbData);
    }
}

