package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.demo.model.MoodEntry;
import com.example.demo.repository.MoodRepository;
import org.springframework.data.domain.PageRequest;

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
            .system("Analyze the user's mood. It is okay if the mood is negative. We want to display their raw emotions through art. " +
                    "translate their emotion into an artistic concept in a few words ")
            .user(userText)
            .call()
            .content()
            .trim();
    }

    public MoodEntry createAndSaveMoodEntry (String userText) {
        String analyzedMood = analyzeMood(userText);
        String prompt = "Create an artistic representation of the user's mood description as an oil painting. No words on in your result. Mood: " + analyzedMood;
        
        // URL "/images/mood-xyz.png" instead of Base64.
        String imagePath = bananaArtService.generate(prompt);
        
        // Pass the path to the new constructor
        MoodEntry entry = new MoodEntry(userText, "Detected Mood: " + analyzedMood, imagePath);
        return moodRepository.save(entry);
    }

    public MoodEntry createCollageFromHistory(int k){
        List<MoodEntry> history = moodRepository.findTopByOrderByCreatedAtDesc(PageRequest.of(0, k));

        if (history.isEmpty()) {
            throw new RuntimeException("Not enough history to create a collage.");
        }
        String combinedHistory = history.stream().map(MoodEntry::getUserMessage).collect(Collectors.joining(" | "));

        String arcSummary = chatClient.prompt()
            .system("You are an expert at summarizing user moods into an artistic concept. " +
                    "Based on the following user mood history, provide a concise artistic theme that captures the overall sentiment. Keep all art in the style of an oil painting.: ")
            .user(combinedHistory)
            .call()
            .content()
            .trim();
        
        String collagePrompt = "Create a collage that represents the following artistic theme: " + arcSummary;

        String imagePath = bananaArtService.generate(collagePrompt);

        MoodEntry collageEntry = new MoodEntry("Collage representing: " + arcSummary, "Collage Mood: " + arcSummary, imagePath);
        return moodRepository.save(collageEntry);

    }
}
