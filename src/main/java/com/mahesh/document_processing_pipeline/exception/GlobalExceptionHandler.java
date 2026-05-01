package com.mahesh.document_processing_pipeline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(DocumentNotFoundException ex){
        return
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
}
}
