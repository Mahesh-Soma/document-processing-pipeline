package document_processing_pipeline.service;

import com.mahesh.document_processing_pipeline.exception.DocumentNotFoundException;
import com.mahesh.document_processing_pipeline.exception.FileValidationException;
import com.mahesh.document_processing_pipeline.repository.DocumentRepository;
import com.mahesh.document_processing_pipeline.service.DocumentProcessorService;
import com.mahesh.document_processing_pipeline.service.DocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceImplTest {

    @Mock
    private DocumentRepository repository;
    @Mock
    private DocumentProcessorService documentProcessorService;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Test
    void uploadDocument_shouldThrowException_whenFileIsEmpty() {
        MultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.pdf", "application/pdf", new byte[0]);

        assertThrows(FileValidationException.class, () -> {
            documentService.uploadDocument(emptyFile);
        });
    }

    @Test
    void uploadDocument_shouldThrowException_whenFileIsNotPdf() {
        MultipartFile textFile = new MockMultipartFile(
                "file", "notes.txt", "text/plain", "hello world".getBytes());

        assertThrows(FileValidationException.class, () -> {
            documentService.uploadDocument(textFile);
        });
    }

    @Test
    void uploadDocument_shouldThrowException_whenFileExceedsSizeLimit() {
        byte[] largeContent = new byte[6 * 1024 * 1024];
        MultipartFile largeFile = new MockMultipartFile(
                "file", "big.pdf", "application/pdf", largeContent);

        assertThrows(FileValidationException.class, () -> {
            documentService.uploadDocument(largeFile);
        });
    }

    @Test
    void getDocument_shouldThrowException_whenDocumentNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class, () -> {
            documentService.getDocument(999L);
        });
    }

}
