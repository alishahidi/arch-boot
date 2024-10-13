package com.alishahidi.api.test;

import com.alishahidi.api.core.exception.ExceptionTemplate;
import com.alishahidi.api.core.exception.ExceptionUtil;
import com.alishahidi.api.core.i18n.I18nUtil;
import com.alishahidi.api.core.image.ImageProcessor;
import com.alishahidi.api.core.pdf.Pdf;
import com.alishahidi.api.core.pdf.PdfSelection;
import com.alishahidi.api.core.response.ApiResponse;
import com.alishahidi.api.core.s3.Bucket;
import com.alishahidi.api.core.s3.config.S3LiaraConfig;
import com.alishahidi.api.core.s3.strategy.StandardBucketStrategy;
import com.alishahidi.api.core.util.FileDetails;
import com.alishahidi.api.core.util.IOUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/hello")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HelloController {

    I18nUtil i18nUtil;
    S3LiaraConfig s3LiaraConfig;

    @GetMapping("/")
    public ApiResponse<String> hello() {
        return ApiResponse.success("Hii");
    }

    @GetMapping("/error")
    public ApiResponse<String> error() {
        Boolean a = true;

        if (a) {
            throw ExceptionUtil.make(ExceptionTemplate.PERSON_NOT_FOUND);
        }

        return ApiResponse.success("Hii");
    }

    @PostMapping("/validate")
    public String validate(@RequestBody @Valid TestDto testDto) {
        return i18nUtil.getMessage("validation.notEmpty");
    }

    @Async
    @PostMapping("/upload")
    public CompletableFuture<String> upload(@RequestParam("file") MultipartFile file) {
        Bucket bucket = Bucket.builder()
                .name("contract")
                .strategy(new StandardBucketStrategy())
                .config(s3LiaraConfig)
                .build();

        bucket.put("test.jpg", IOUtils.multipartFileToPath(file));

        return CompletableFuture.completedFuture("sdsd");
    }

    @PostMapping("/pdf/image/all")
    public CompletableFuture<String> pdfImagesAll(@RequestParam("file") MultipartFile pdf) throws IOException {
        Bucket bucket = Bucket.builder()
                .name("contract")
                .strategy(new StandardBucketStrategy())
                .config(s3LiaraConfig)
                .build();

        Pdf.create()
                .selection(PdfSelection.ALL)
                .toImage(IOUtils.multipartFileToPath(pdf))
                .thenAccept(paths -> {
                    paths.forEach(path -> bucket.put(path.getFileName().toString(), path));
                });

        return CompletableFuture.completedFuture("sds");
    }

    @PostMapping("/fileDetail")
    public ApiResponse<FileDetails> fileDetail(@RequestParam("file") MultipartFile file) {
        return ApiResponse.success(IOUtils.fileDetails(IOUtils.multipartFileToPath(file)));
    }

    @PostMapping("/compressImage")
    public ApiResponse<String> compressImage(@RequestParam("file") MultipartFile file) {
        FileDetails fileDetails = IOUtils.fileDetails(IOUtils.multipartFileToPath(file));
        FileDetails newFileDetails = IOUtils.fileDetails(ImageProcessor.create()
                .process(IOUtils.multipartFileToPath(file)));

        return ApiResponse.success(fileDetails.getSize() + "   --   " + newFileDetails.getSize());
    }
}
