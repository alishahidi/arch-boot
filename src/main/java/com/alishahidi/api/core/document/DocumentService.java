package com.alishahidi.api.core.document;

import com.alishahidi.api.core.exception.ExceptionTemplate;
import com.alishahidi.api.core.exception.ExceptionUtil;
import com.alishahidi.api.core.garbagecollector.collector.DocumentExpiredGarbageCollector;
import com.alishahidi.api.core.image.ImageProcessor;
import com.alishahidi.api.core.response.ApiResponse;
import com.alishahidi.api.core.s3.Bucket;
import com.alishahidi.api.core.s3.S3Scope;
import com.alishahidi.api.core.s3.config.S3LiaraConfig;
import com.alishahidi.api.core.s3.strategy.UniqueDateFolderBucketStrategy;
import com.alishahidi.api.core.security.jwt.JwtService;
import com.alishahidi.api.core.util.FileDetails;
import com.alishahidi.api.core.util.FileType;
import com.alishahidi.api.core.util.IOUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentService {

    final S3LiaraConfig s3LiaraConfig;
    final DocumentRepository documentRepository;
    final JwtService jwtService;
    final DocumentExpiredGarbageCollector documentExpiredGarbageCollector;

    @Value("${document.jwt.exp}")
    Long documentExp;

    @Async
    public CompletableFuture<Document> get(Long id) {
        Optional<Document> document = documentRepository.findById(id);

        if (document.isPresent()) {
            return CompletableFuture.completedFuture(document.get());
        }

        throw ExceptionUtil.make(ExceptionTemplate.ENTITY_NOT_FOUND);
    }

    @Async
    public CompletableFuture<ApiResponse<DocumentDto>> upload(MultipartFile file, String scope) {
        S3Scope s3Scope = S3Scope.fromScope(scope);
        if (s3Scope.equals(S3Scope.UNKNOWN)) {
            throw ExceptionUtil.make(ExceptionTemplate.S3_SCOPE_ERROR);
        }

        Path tmpPath = IOUtils.multipartFileToPath(file);
        FileDetails fileDetails = IOUtils.fileDetails(tmpPath);
        if (fileDetails.getType().equals(FileType.IMAGE)) {
            Path compressImage = ImageProcessor.create().process(tmpPath);
            IOUtils.deleteFile(tmpPath);
            tmpPath = compressImage;
        }

        Bucket bucket = Bucket.builder()
                .name(s3Scope.getBucket())
                .strategy(new UniqueDateFolderBucketStrategy())
                .config(s3LiaraConfig)
                .build();

        String key = bucket.put(file.getOriginalFilename(), tmpPath, "upload").join();

        IOUtils.deleteFile(tmpPath);

        Document document = Document.builder()
                .type(fileDetails.getType())
                .mimeType(fileDetails.getMimeType())
                .extension(fileDetails.getExtension())
                .size(fileDetails.getSize())
                .scope(s3Scope)
                .path(key)
                .build();

        Document savedDocument = documentRepository.save(document);
        DocumentDto documentDto = DocumentMapper.INSTANCE.toDto(savedDocument);

        documentExpiredGarbageCollector.link(savedDocument);

        return CompletableFuture.completedFuture(ApiResponse.success(documentDto));
    }

    @Async
    public CompletableFuture<ApiResponse<String>> link(@PathVariable Long id) {
        Document document = get(id).join();

        return CompletableFuture.completedFuture(ApiResponse.success(jwtService.generateToken(String.valueOf(document.getId()), documentExp)));
    }

    public Mono<ResponseEntity<StreamingResponseBody>> stream(@PathVariable String token) {
        Long id = Long.valueOf(jwtService.getSubject(token));
        return
                Mono.fromFuture(() -> get(id))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(document -> {
                            Bucket bucket = Bucket.builder()
                                    .name(document.getScope().getBucket())
                                    .strategy(new UniqueDateFolderBucketStrategy())
                                    .config(s3LiaraConfig)
                                    .build();

                            return
                                    bucket.get(document).map(inputStream -> ResponseEntity.ok()
                                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + IOUtils.extractFileNameWithS3Key(document.getPath()) + "\"")
                                            .body(outputStream -> {
                                                byte[] buffer = new byte[8192];
                                                int bytesRead;
                                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                                    outputStream.write(buffer, 0, bytesRead);
                                                }
                                            }));
                        });
    }
}