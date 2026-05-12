package com.mahesh.document_processing_pipeline.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TextExtractionService {

    private static final Logger log =
            LoggerFactory.getLogger(TextExtractionService.class);

    public String extractText(String filePath) {

        try {

            File file = new File(filePath);

            log.info("Processing file: {}", file.getAbsolutePath());

            log.info("File exists: {}", file.exists());

            try (PDDocument document = PDDocument.load(file)) {

                PDFTextStripper stripper = new PDFTextStripper();

                return stripper.getText(document);
            }

        } catch (Exception e) {

            log.error("Error extracting PDF text", e);

            throw new RuntimeException(
                    "Failed to extract text from PDF", e);
        }
    }
}