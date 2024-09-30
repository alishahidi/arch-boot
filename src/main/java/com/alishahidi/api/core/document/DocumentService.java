package com.alishahidi.api.core.document;

import com.alishahidi.api.core.image.ImageProcessor;
import com.alishahidi.api.core.s3.Bucket;
import com.alishahidi.api.core.s3.config.S3LiaraConfig;
import com.alishahidi.api.core.s3.strategy.StandardBucketStrategy;
import com.alishahidi.api.core.s3.strategy.UniqueDateFolderBucketStrategy;
import com.alishahidi.api.core.util.FileDetails;
import com.alishahidi.api.core.util.FileType;
import com.alishahidi.api.core.util.IOUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentService {

    S3LiaraConfig s3LiaraConfig;
    DocumentRepository documentRepository;

    @Async
    public CompletableFuture<DocumentDto> upload(MultipartFile file) {
        Path tmpPath = IOUtils.multipartFileToPath(file);
        FileDetails fileDetails = IOUtils.fileDetails(tmpPath);
        if (fileDetails.getType().equals(FileType.IMAGE)) {
            Path compressImage = ImageProcessor.create().process(tmpPath);
            IOUtils.deleteFile(tmpPath);
            tmpPath = compressImage;
        }

        Bucket bucket = Bucket.builder()
                .name("contract")
                .strategy(new UniqueDateFolderBucketStrategy())
                .config(s3LiaraConfig)
                .build();

        String key = bucket.put(file.getOriginalFilename(), tmpPath, "upload").join();

        IOUtils.deleteFile(tmpPath);

        Document document = Document.builder()
                .type(fileDetails.getType().getName())
                .extension(fileDetails.getExtension())
                .size(fileDetails.getSize())
                .path(key)
                .build();

        Document savedDocument = documentRepository.save(document);
        DocumentDto documentDto = DocumentMapper.INSTANCE.toDto(savedDocument);

        return CompletableFuture.completedFuture(documentDto);
    }
}
