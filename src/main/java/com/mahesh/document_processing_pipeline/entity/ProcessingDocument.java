package com.mahesh.document_processing_pipeline.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class ProcessingDocument {



    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String filePath;
    private String status;   //uploaded,PROCESSING,COMPLETED,FAILED

    private Long fileSize;
    private  String extractedText;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}
