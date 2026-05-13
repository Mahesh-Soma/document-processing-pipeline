package com.mahesh.document_processing_pipeline.service;

import com.mahesh.document_processing_pipeline.dto.DocumentResponseDTO;
import com.mahesh.document_processing_pipeline.entity.ProcessingDocument;
import com.mahesh.document_processing_pipeline.exception.DocumentNotFoundException;
import com.mahesh.document_processing_pipeline.exception.FileValidationException;
import com.mahesh.document_processing_pipeline.exception.ProcessingException;
import com.mahesh.document_processing_pipeline.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
 @Slf4j
@Service
public class DocumentServiceimpl implements DocumentService {
     //private static final Logger log = LoggerFactory.getLogger(DocumentServiceimpl.class);

    @Autowired
    private DocumentRepository repository;
     @Autowired
     private DocumentProcessorService documentProcessorService;

    @Override
    public DocumentResponseDTO uploadDocument(MultipartFile file) {
        try {
            //Empty file validation
            if (file.isEmpty()) {
                throw new FileValidationException("Uploaded file is empty");
            }

            //file type should be pdf
            if(!"application/pdf".equals(file.getContentType())){
                throw new FileValidationException("only PDF files are allowed");
            }
            // FILE SIZE VALIDATION
            long maxFileSize = 5 * 1024 * 1024;

            if (file.getSize() > maxFileSize) {
                throw new FileValidationException("File size exceeds 5 MB limit");
            }


            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File directory = new File(uploadDir);


            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath =
                    uploadDir + System.currentTimeMillis()
                            + "_" + file.getOriginalFilename();

            file.transferTo(new File(filePath));

            ProcessingDocument doc = new ProcessingDocument();

            doc.setFileName(file.getOriginalFilename());
            doc.setFileType(file.getContentType());
            doc.setFileSize(file.getSize());
            doc.setFilePath(filePath);

            doc.setStatus("QUEUED");

            doc.setCreatedAt(LocalDateTime.now());
            doc.setUpdatedAt(LocalDateTime.now());

            repository.save(doc);

            // ASYNC PROCESSING
            documentProcessorService.processDocument(doc);

            DocumentResponseDTO response = new DocumentResponseDTO();

            response.setId(doc.getId());
            response.setStatus(doc.getStatus());
            response.setAiSummary(doc.getAiSummary());
            response.setDocumentCategory(doc.getDocumentCategory());

            return response;

        }
        catch (FileValidationException e) {

            throw e;
        }
        catch (Exception e) {

            log.error("File upload failed", e);

            throw new ProcessingException(
                    "Internal error while uploading document", e);
        }
    }



     public ProcessingDocument getDocument(Long id) {
         return repository.findById(id)
                 .orElseThrow(() -> new DocumentNotFoundException("document not found with id"+id));
     }
}


