package com.mahesh.document_processing_pipeline.service;

import com.mahesh.document_processing_pipeline.entity.ProcessingDocument;
import com.mahesh.document_processing_pipeline.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@Slf4j
public class DocumentProcessorService {
    @Autowired
    private DocumentRepository repository;

    @Autowired
    private TextExtractionService textExtractionService;

    @Autowired
    private AIService aiService;



    @Async
    public void processDocument(ProcessingDocument doc) {
        try {

            Thread.sleep(10000);

            log.info(" Starting processing for docId: {}", doc.getId());

            doc.setStatus("PROCESSING");
            repository.save(doc);
            //STEP 1: TEXT EXTRACTION
            log.info(" Step 1: Extracting text for docId: {}", doc.getId());

            String extractedText = textExtractionService.extractText(doc.getFilePath());

            if (extractedText == null || extractedText.isEmpty()) {
                throw new RuntimeException("Extracted text is empty");
            }

            log.info(" Text extracted. Length: {}", extractedText.length());

            doc.setExtractedText(extractedText);

            // ---------------- STEP 2: AI CALL ----------------
            log.info(" Step 2: Calling AI service for docId: {}", doc.getId());

            String aiResult = aiService.generateSummaryAndTag(extractedText);

            if (aiResult == null || aiResult.isEmpty()) {
                throw new RuntimeException("AI result is empty");
            }

            log.info("AI response received");

            doc.setAiSummary(aiResult);
            doc.setDocumentCategory("AUTO");

            // ---------------- SUCCESS ----------------
            doc.setStatus("COMPLETED");
            doc.setUpdatedAt(LocalDateTime.now());
            repository.save(doc);

            log.info(" Processing completed for docId: {}", doc.getId());

        } catch (Exception e) {

            log.error(" Processing failed for docId: {}", doc.getId(), e);

            doc.setStatus("FAILED");
            doc.setUpdatedAt(LocalDateTime.now());
            repository.save(doc);
        }



    }



}
