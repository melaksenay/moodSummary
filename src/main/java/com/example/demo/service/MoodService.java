package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.demo.model.MoodEntry;
import com.example.demo.repository.MoodRepository;

@Service
public class MoodService {
    
    private final BananaArtService bananaArtService;
    private final MoodRepository moodRepository;
    private final ChatClient chatClient;

    public MoodService(BananaArtService bananaArtService, MoodRepository moodRepository, ChatClient.Builder chatBuilder) {
        this.bananaArtService = bananaArtService;
        this.moodRepository = moodRepository;
        this.chatClient = chatBuilder.build();
    }

    public String analyzeMood(String userText) {
        String prompt = "Analyze the mood of the following text and respond with a single word describing the mood: ";
        return chatClient.prompt().system(prompt).user(userText).call().content().trim(); // .user() for user input
    }

    public MoodEntry createAndSaveMoodEntry (String userText) {
        String analyzedMood = analyzeMood(userText);
        String prompt = "Create an artistic representation of the user's mood description: " + analyzedMood;
        String base64Image = bananaArtService.generate(prompt);
        MoodEntry entry = new MoodEntry(userText, "Detected Mood: " + analyzedMood, base64Image);
        return moodRepository.save(entry);
    }
}
