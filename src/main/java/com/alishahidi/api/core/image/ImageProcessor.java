package com.alishahidi.api.core.image;

import com.alishahidi.api.core.context.SpringContext;
import com.alishahidi.api.core.exception.ExceptionTemplate;
import com.alishahidi.api.core.exception.ExceptionUtil;
import com.alishahidi.api.core.util.FileDetails;
import com.alishahidi.api.core.util.FileType;
import com.alishahidi.api.core.util.IOUtils;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class ImageProcessor {

    private Double scale;
    private Double ratio;
    private Integer width;
    private Integer height;
    private Watermark watermark;
    private boolean copyright = false;

    private ImageProcessor() {
        ImageConfig imageConfig = SpringContext.getBean(ImageConfig.class);

        this.scale = imageConfig.getScale();
        this.ratio = imageConfig.getRatio();
    }

    public static ImageProcessor create() {
        return new ImageProcessor();
    }

    public ImageProcessor copyright() {
        this.copyright = true;
        return this;
    }

    public ImageProcessor watermark(Watermark watermark) {
        this.watermark = watermark;
        return this;
    }

    public ImageProcessor scale(Double scale) {
        this.scale = scale;
        return this;
    }

    public ImageProcessor ratio(Double ratio) {
        this.ratio = ratio;
        return this;
    }

    public ImageProcessor width(Integer width) {
        this.width = width;
        return this;
    }

    public ImageProcessor height(Integer height) {
        this.height = height;
        return this;
    }

    public Path process(Path inputPath) {
        validateInput(inputPath);
        FileDetails fileDetails = IOUtils.fileDetails(inputPath);

        if (fileDetails.getType().equals(FileType.IMAGE)) {
            return processImage(inputPath, fileDetails.getExtension().trim().replaceAll("\\.", ""));
        } else {
            throw ExceptionUtil.make(ExceptionTemplate.IMAGE_TYPE_ERROR);
        }
    }

    private Path processImage(Path inputPath, String extension) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.Builder<?> finalBuilder = Thumbnails.of(inputPath.toFile());

            if (watermark != null) {
                BufferedImage transformedImage = Thumbnails.of(inputPath.toFile())
                        .scale(1.0)
                        .asBufferedImage();
                BufferedImage watermarkedImage = watermark.applyWatermark(transformedImage, copyright);

                finalBuilder = Thumbnails.of(watermarkedImage);
            }

            applyTransformations(finalBuilder);

            finalBuilder
                    .outputFormat(extension)
                    .toOutputStream(outputStream);

            return createTempFile(outputStream, extension);
        } catch (Exception e) {
            throw ExceptionUtil.make(ExceptionTemplate.FILE_PROCESS);
        }
    }


    private void validateInput(Path inputPath) {
        if (inputPath == null || !Files.exists(inputPath) || !Files.isRegularFile(inputPath)) {
            throw ExceptionUtil.make(ExceptionTemplate.FILE_PROCESS);
        }
    }

    private void applyTransformations(Thumbnails.Builder<?> fileBuilder) {
        if (scale != null) {
            fileBuilder.scale(scale);
        }
        if (ratio != null) {
            fileBuilder.outputQuality(ratio);
        }
        if (width != null && height != null) {
            fileBuilder.size(width, height);
        }
    }

    private Path createTempFile(ByteArrayOutputStream outputStream, String extension) {
        try {
            Path tempFilePath = Files.createTempFile(UUID.randomUUID() + "-", extension);
            Files.write(tempFilePath, outputStream.toByteArray());
            return tempFilePath;
        } catch (Exception e) {
            throw ExceptionUtil.make(ExceptionTemplate.FILE_PROCESS);
        }
    }
}
