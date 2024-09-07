package com.alishahidi.api.test;

import com.alishahidi.api.core.i18n.I18nUtil;
import com.alishahidi.api.core.pdf.Pdf;
import com.alishahidi.api.core.pdf.PdfSelection;
import com.alishahidi.api.core.s3.config.S3LiaraConfig;
import com.alishahidi.api.core.util.IOUtils;
import com.alishahidi.api.core.s3.Bucket;
import com.alishahidi.api.core.s3.strategy.StandardBucketStrategy;
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
    public String hello() {
        return i18nUtil.getMessage("tag.hello", "Ali");
    }

    @PostMapping("/validate")
    public String validate(@RequestBody @Valid TestDto testDto) {
        return i18nUtil.getMessage("validation.notEmpty");
    }

    @Async
    @PostMapping("/upload")
    public CompletableFuture<String> upload(@RequestParam("file") MultipartFile file) {
        Bucket bucket = Bucket.builder()
                .name("bucket")
                .strategy(new StandardBucketStrategy())
                .config(s3LiaraConfig)
                .build();

        bucket.put("test.jpg", IOUtils.multipartFileToPath(file));

        return CompletableFuture.completedFuture("sdsd");
    }

    @PostMapping("/pdf/image/all")
    public CompletableFuture<String> pdfImagesAll(@RequestParam("file") MultipartFile pdf) throws IOException {
        Bucket bucket = Bucket.builder()
                .name("bucket")
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

}
