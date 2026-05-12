package com.mahesh.document_processing_pipeline;


//import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class DocumentProcessingPipelineApplication {

	public static void main(String[] args) {


		SpringApplication.run(DocumentProcessingPipelineApplication.class, args);
	}
}
