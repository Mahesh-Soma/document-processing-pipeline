package com.mahesh.document_processing_pipeline.exception;

public class AIProcessingException extends RuntimeException   {

    public AIProcessingException(String message){
        super(message);
    }
    public AIProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

}
