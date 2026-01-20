package com.example.demo.model;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "mood_gallery")
public class MoodEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userMessage;

    private String moodDescription;

    @Column(columnDefinition = "TEXT")
    private String imageBase64;

    private LocalDateTime createdAt;

    public MoodEntry() {}

    public MoodEntry(String userMessage, String moodDescription, String imageBase64) {
        this.userMessage = userMessage;
        this.moodDescription = moodDescription;
        this.imageBase64 = imageBase64;
        this.createdAt = LocalDateTime.now();
    }

public Long getId() { return id; }
    public String getUserMessage() { return userMessage; }
    public String getMoodDescription() { return moodDescription; }
    public String getImageBase64() { return imageBase64; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}