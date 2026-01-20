package com.example.demo.service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class BananaArtService {

    private final Client client;

    public BananaArtService(Client client){
        this.client = client;
    }

    public String generate(String prompt){
        // image response
        GenerateContentConfig config = GenerateContentConfig.builder().responseModalities("IMAGE").build();
        
        try {
            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash-image", prompt, config);

            //extract image bytes
            for (Part part : response.parts()){
                if(part.inlineData().isPresent()){
                    byte[] imageBytes = part.inlineData().get().data().get();
                    String base64 = Base64.getEncoder().encodeToString(imageBytes);

                    return "data:image/png;base64," + base64;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to paint art with error: " + e.getMessage());
        }
        return "No image found.";

    }

    
}
