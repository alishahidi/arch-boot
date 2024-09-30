package com.alishahidi.api.core.util;

import com.alishahidi.api.core.exception.ExceptionTemplate;
import com.alishahidi.api.core.exception.ExceptionUtil;
import lombok.experimental.UtilityClass;
import org.apache.tika.Tika;
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

    public FileDetails fileDetails(Path filePath) {
        try {
            long size = Files.size(filePath);

            Tika tika = new Tika();
            String type = tika.detect(filePath);

            return FileDetails.builder()
                    .size(size)
                    .type(type)
                    .build();
        } catch (IOException e) {
            throw ExceptionUtil.make(ExceptionTemplate.FILE_PROCESS);
        }
    }
}
