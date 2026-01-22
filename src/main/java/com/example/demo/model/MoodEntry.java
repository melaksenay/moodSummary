package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mood_gallery")
public class MoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userMessage;
    private String moodDescription;

    //imageBase64 to imagePath
    private String imagePath; 

    private LocalDateTime createdAt;

    public MoodEntry() {}

    public MoodEntry(String userMessage, String moodDescription, String imagePath) {
        this.userMessage = userMessage;
        this.moodDescription = moodDescription;
        this.imagePath = imagePath;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUserMessage() { return userMessage; }
    public String getMoodDescription() { return moodDescription; }
    public String getImagePath() { return imagePath; } // Update getter name
    public LocalDateTime getCreatedAt() { return createdAt; }
}