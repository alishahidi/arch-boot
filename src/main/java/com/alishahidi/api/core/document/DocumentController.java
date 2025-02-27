package com.alishahidi.api.core.document;

import com.alishahidi.api.core.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentController {
    DocumentService documentService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ApiResponse<DocumentDto>> upload(@RequestBody MultipartFile file, @RequestParam(required = false, defaultValue = "private-contract") String scope) {
        return documentService.upload(file, scope);
    }

    @GetMapping("/token/{id}")
    public CompletableFuture<ApiResponse<String>> token(@PathVariable Long id) {
        return documentService.link(id);
    }

    @GetMapping("/stream/{token}")
    public Mono<ResponseEntity<StreamingResponseBody>> token(@PathVariable String token) {
        return documentService.stream(token);
    }
}
