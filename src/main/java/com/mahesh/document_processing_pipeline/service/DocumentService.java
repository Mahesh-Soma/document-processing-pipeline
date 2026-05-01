package com.mahesh.document_processing_pipeline.service;

import com.mahesh.document_processing_pipeline.dto.DocumentResponseDTO;
import com.mahesh.document_processing_pipeline.entity.ProcessingDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    DocumentResponseDTO uploadDocument(MultipartFile file);
    ProcessingDocument getDocument(Long id);


}
