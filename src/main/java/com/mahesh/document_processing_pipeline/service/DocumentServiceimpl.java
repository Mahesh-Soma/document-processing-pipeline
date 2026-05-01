package com.mahesh.document_processing_pipeline.service;

import com.mahesh.document_processing_pipeline.dto.DocumentResponseDTO;
import com.mahesh.document_processing_pipeline.entity.ProcessingDocument;
import com.mahesh.document_processing_pipeline.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class DocumentServiceimpl implements DocumentService {
    @Autowired
    private DocumentRepository repository;

    @Override
    public DocumentResponseDTO uploadDocument(MultipartFile file) {

        ProcessingDocument doc = new ProcessingDocument();
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setFileSize(file.getSize());
        doc.setStatus("UPLOADED");
        doc.setCreatedAt(LocalDateTime.now());
        repository.save(doc);

        //Trigger processing
        processDocument(doc, file);

        DocumentResponseDTO response = new DocumentResponseDTO();
        response.setId(doc.getId());
        response.setStatus(doc.getStatus());

        return response;

    }

    private void processDocument(ProcessingDocument doc, MultipartFile file) {
        try {
            doc.setStatus("PROCESSING");
            repository.save(doc);
            String content = new String(file.getBytes());
            doc.setExtractedText(content);
            doc.setStatus("COMPLETED");
            doc.setUpdatedAt(LocalDateTime.now());

        } catch (Exception e) {
            doc.setStatus("FAILED");
        }
        repository.save(doc);
    }

    public ProcessingDocument getDocument(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

}


