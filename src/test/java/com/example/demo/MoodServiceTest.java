package com.example.demo;

import com.example.demo.model.MoodEntry;
import com.example.demo.repository.MoodRepository;
import com.example.demo.service.BananaArtService;
import com.example.demo.service.MoodService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoodServiceTest {

    @Mock
    private MoodRepository moodRepository;

    @Mock
    private BananaArtService bananaArtService;

    @Mock
    private ChatClient.Builder chatBuilder;

    // Use Deep Stubs for the Fluent API
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    private MoodService moodService;

    @BeforeEach
    void setUp() {
        // Simulate the chatBuilder.build() call from your constructor
        when(chatBuilder.build()).thenReturn(chatClient);
        moodService = new MoodService(bananaArtService, moodRepository, chatBuilder);
    }

    @Test
    void testCreateCollage_WithEmptyHistory_ShouldThrowException() {
        // Arrange: Match the exact PageRequest signature from your code
        when(moodRepository.findTopByOrderByCreatedAtDesc(PageRequest.of(0, 3)))
                .thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            moodService.createCollageFromHistory(3);
        });
        assertEquals("Not enough history to create a collage.", exception.getMessage());
    }

    @Test
    void testCreateCollage_SuccessfulFlow() {
        // 1.Match the exact PageRequest for k=1
        MoodEntry entry1 = new MoodEntry("I am happy", "Bright art", "path1.png");
        when(moodRepository.findTopByOrderByCreatedAtDesc(PageRequest.of(0, 1)))
                .thenReturn(List.of(entry1));

        // Ai response
        when(chatClient.prompt().system(anyString()).user("I am happy").call().content())
                .thenReturn(" Mocked AI Summary. ");

        // 3. Arrange Image Generator
        when(bananaArtService.generate(anyString())).thenReturn("/images/collage.png");

        // 4. Arrange DB Save
        when(moodRepository.save(any(MoodEntry.class))).thenAnswer(i -> i.getArgument(0));

        // mock the call
        MoodEntry result = moodService.createCollageFromHistory(1);

        // Assert
        assertNotNull(result, "Result should not be null!");
        assertEquals("/images/collage.png", result.getImagePath());
        
        // Verifying that .trim() worked
        assertEquals("Collage Mood: Mocked AI Summary.", result.getMoodDescription());
    }
}