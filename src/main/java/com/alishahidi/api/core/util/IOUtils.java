package com.alishahidi.api.core.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@UtilityClass
public class IOUtils {

    public Path multipartFileToPath(MultipartFile file) {
        try {
            Path path = Files.createTempFile(UUID.randomUUID() + "-", file.getOriginalFilename());
            file.transferTo(path);

            return path;
        } catch (IOException e) {
            //todo
            return null;
        }
    }

    public void deleteFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
