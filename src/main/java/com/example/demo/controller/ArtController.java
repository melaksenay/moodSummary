package com.example.demo.controller;

import com.example.demo.model.MoodEntry;
import com.example.demo.repository.MoodRepository;
import com.example.demo.service.MoodService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
public class ArtController {

    private final MoodService moodService;
    private final MoodRepository moodRepository;

    public ArtController(MoodService moodService, MoodRepository moodRepository) {
        this.moodService = moodService;
        this.moodRepository = moodRepository;
    }

    // save mood to db.
    @PostMapping("/mood")
    public MoodEntry submitMood(@RequestBody String userText) {
        return moodService.createAndSaveMoodEntry(userText);
    }

    // get all moods from db.
    @GetMapping("/history")
    public List<MoodEntry> getHistory() {
        return moodRepository.findAll();
    }

}