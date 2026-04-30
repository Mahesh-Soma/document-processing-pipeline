package com.mahesh.document_processing_pipeline.repository;

import com.mahesh.document_processing_pipeline.entity.ProcessingDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<ProcessingDocument,Long> {
}
