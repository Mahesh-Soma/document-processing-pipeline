package com.mahesh.document_processing_pipeline.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class TextExtractionService {


    public String extractText(MultipartFile file) {


        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);


            if (text == null || text.trim().isEmpty()) {
                throw new RuntimeException("Extracted text is empty (possibly scanned PDF)");
            }

            return text;



        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text from PDF");
        }
    }


}
