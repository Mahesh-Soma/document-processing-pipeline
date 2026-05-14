package com.mahesh.document_processing_pipeline.service;

import com.mahesh.document_processing_pipeline.dto.AIResponseDTO;
import com.mahesh.document_processing_pipeline.exception.AIProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${openai.api.key}")
    private String apiKey;


    @Autowired
    private RestTemplate restTemplate;

    public AIResponseDTO generateSummaryAndTag(String text) {
        System.out.println("KEY START = " + apiKey.substring(0, 10));

        if (text == null || text.trim().isEmpty()) {
            return new AIResponseDTO(
                    "EMPTY DOCUMENT",
                    "OTHER"
            );

        }

        text = text.substring(0, Math.min(text.length(), 3000));

        try {

            String url = "https://api.openai.com/v1/responses";

            Map<String, Object> request = new HashMap<>();
            request.put("model", "gpt-4.1-mini");
            request.put("input",
                    """
                    Return response ONLY in this format:

                    SUMMARY: <summary>
                    CATEGORY: <category>

                    Categories allowed:
                    INVOICE, RESUME, CONTRACT, OTHER

                    Document:
                    """ + text
            );



            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(request, headers);

            // OPENAI CALL
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, entity, Map.class);

            if (response.getBody() == null) {
                throw new AIProcessingException("Empty response from OpenAI");
            }

            Map responseBody = response.getBody();

            List output = (List) responseBody.get("output");
            Map firstOutput = (Map) output.get(0);

            List content = (List) firstOutput.get("content");
            Map contentItem = (Map) content.get(0);

            String aiText = contentItem.get("text").toString();
            String summary = "";
            String category = "";
            String[] lines = aiText.split("\n");

            for(String line : lines){

                if(line.startsWith("SUMMARY:")){
                    summary = line.replace("SUMMARY:", "").trim();
                }

                if(line.startsWith("CATEGORY:")){
                    category = line.replace("CATEGORY:", "").trim();
                }
            }

            if(summary.isEmpty()){
                summary = "No summary generated";
            }

            if(category.isEmpty()){
                category = "OTHER";
            }

            return new AIResponseDTO(summary, category);

        } catch (Exception e) {

            System.out.println("OPENAI FAILED -> USING FALLBACK");

            String lowerText = text.toLowerCase();

            String category;

            if (lowerText.contains("invoice")) {
                category = "INVOICE";
            } else if (lowerText.contains("resume")) {
                category = "RESUME";
            } else if (lowerText.contains("contract")) {
                category = "CONTRACT";
            } else {
                category = "OTHER";
            }

            String summary =
                    text.substring(0, Math.min(text.length(), 100));

            return new AIResponseDTO(summary,category);
        }


    }
}