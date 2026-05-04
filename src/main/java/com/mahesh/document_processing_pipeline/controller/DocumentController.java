package com.mahesh.document_processing_pipeline.controller;

import com.mahesh.document_processing_pipeline.dto.DocumentResponseDTO;
import com.mahesh.document_processing_pipeline.entity.ProcessingDocument;
import com.mahesh.document_processing_pipeline.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/documents")
public class DocumentController {
    @Autowired
    private DocumentService service;

    @PostMapping("/upload")
    public DocumentResponseDTO upload(@RequestParam("file") MultipartFile file){
        return service.uploadDocument(file);

    }
    @GetMapping("/{id}")
    public ProcessingDocument get(@PathVariable Long id){
        return service.getDocument(id);
    }
}
