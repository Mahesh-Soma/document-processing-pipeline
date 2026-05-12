package com.mahesh.document_processing_pipeline.controller;

import com.mahesh.document_processing_pipeline.dto.DocumentResponseDTO;
import com.mahesh.document_processing_pipeline.entity.ProcessingDocument;
import com.mahesh.document_processing_pipeline.service.DocumentService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/documents")
public class DocumentController {
    @Autowired
    private DocumentService service;

    @PostConstruct
    public void init() {

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String apiKey = dotenv.get("OPENAI_API_KEY");
        if (apiKey != null) {
            System.out.println("SUCCESS: OPENAI_API_KEY loaded from .env");
        } else {

            apiKey = System.getenv("OPENAI_API_KEY");
            System.out.println(" INFO: Key not in .env, checking System: " + (apiKey != null ? "Found" : "Not Found"));
        }

    }
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public DocumentResponseDTO upload(@RequestParam("file") MultipartFile file){
        return service.uploadDocument(file);

    }
    @GetMapping("/{id}")
    public ProcessingDocument get(@PathVariable Long id){
        return service.getDocument(id);
    }

}
