package com.alishahidi.api.core.util;

import com.alishahidi.api.core.exception.ExceptionTemplate;
import com.alishahidi.api.core.exception.ExceptionUtil;
import lombok.experimental.UtilityClass;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

            MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();
            MimeType mimeType;
            String extension;
            try {
                mimeType = mimeTypes.forName(type);
                extension = mimeType.getExtension();
            } catch (MimeTypeException e) {
                extension = "unknown";
            }
            return FileDetails.builder()
                    .size(size)
                    .type(FileType.fromMimeType(type))
                    .mimeType(type)
                    .extension(extension)
                    .build();
        } catch (IOException e) {
            throw ExceptionUtil.make(ExceptionTemplate.FILE_PROCESS);
        }
    }

    public String extractFileNameWithS3Key(String s3Key) {
        return s3Key.substring(s3Key.lastIndexOf("/") + 1);
    }

    public String calculateHash(Path filePath) throws IOException, NoSuchAlgorithmException {
        return calculateHash(filePath, "SHA-256");
    }

    public String calculateHash(Path filePath, String hashAlgorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);

        try (InputStream inputStream = Files.newInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public Path getResourceAsPath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            Path tempFile = Files.createTempFile("resource-", "-" + resource.getFilename());
            Files.copy(inputStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return tempFile;
        }
    }
}
