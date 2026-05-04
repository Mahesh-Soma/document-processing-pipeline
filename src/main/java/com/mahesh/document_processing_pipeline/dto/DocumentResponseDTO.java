package com.mahesh.document_processing_pipeline.dto;

import lombok.Data;

@Data
public class DocumentResponseDTO {
    private Long id;
    private String status;
    private String aiSummary;
    private String documentCategory;
}
