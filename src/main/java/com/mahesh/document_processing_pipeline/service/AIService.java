package com.mahesh.document_processing_pipeline.service;

import org.springframework.stereotype.Service;

@Service
public class AIService {

    public String generateSummaryAndTag(String text) {
        // later connect OpenAI or another AI API
        // for now this will be your AI integration layer
        return "Summary + category from AI";
    }

}
