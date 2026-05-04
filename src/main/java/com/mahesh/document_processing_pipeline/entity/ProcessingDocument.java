package com.mahesh.document_processing_pipeline.entity;

import jakarta.persistence.*;
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
    @Column(columnDefinition = "TEXT")
    private  String extractedText;
    @Column(columnDefinition = "TEXT")
    private String aiSummary;
    private String documentCategory;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}
