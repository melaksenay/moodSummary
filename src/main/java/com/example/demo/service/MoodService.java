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
    return chatClient.prompt()
            .system("Analyze the user's mood. If the mood is negative (stress, anger, sadness), " +
                    "translate it into a constructive artistic concept (e.g., 'Turbulence', 'Solitude', 'Intensity'). " +
                    "Respond with ONLY one word.")
            .user(userText)
            .call()
            .content()
            .trim();
}

    public MoodEntry createAndSaveMoodEntry (String userText) {
        String analyzedMood = analyzeMood(userText);
        String prompt = "Create an artistic representation of the user's mood description: " + analyzedMood;
        
        // URL "/images/mood-xyz.png" instead of Base64.
        String imagePath = bananaArtService.generate(prompt);
        
        // Pass the path to the new constructor
        MoodEntry entry = new MoodEntry(userText, "Detected Mood: " + analyzedMood, imagePath);
        return moodRepository.save(entry);
    }
}
