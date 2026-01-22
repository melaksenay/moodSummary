package com.example.demo.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class BananaArtService {

    private final Client client;
    // get upload dir 
    private final Path uploadDir = Paths.get("uploads");

    public BananaArtService(Client client) {
        this.client = client;
        // Create the folder if it doesn't exist
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String generate(String prompt) {
        GenerateContentConfig config = GenerateContentConfig.builder()
                .responseModalities("IMAGE") 
                .build();

        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash-image", 
                prompt,
                config
        );

        for (Part part : response.parts()) {
            if (part.inlineData().isPresent()) {
                byte[] imageBytes = part.inlineData().get().data().get();
                
                // make mood filename from random uuid: "mood-550e8400.png")
                String fileName = "mood-" + UUID.randomUUID() + ".png";
                
                //Save the file to disk
                Path destination = uploadDir.resolve(fileName);
                try (FileOutputStream fos = new FileOutputStream(destination.toFile())) {
                    fos.write(imageBytes);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image to disk", e);
                }

                //access the image instead of the file path.
                return "/images/" + fileName;
            }
        }
        return null; 
    }
}