package com.alishahidi.api.core.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

public class Watermark {
    private Path watermarkPath;
    private float opacity = 0.5f;
    private int spacing = 50;
    private int resizeWidth = 100;
    private int resizeHeight = 100;
    private WatermarkPosition position = WatermarkPosition.CENTER;

    private Watermark(){
    }

    public Watermark with(Path watermarkPath, float opacity) {
        this.watermarkPath = watermarkPath;
        this.opacity = opacity;
        return this;
    }

    public Watermark position(WatermarkPosition position) {
        this.position = position;
        return this;
    }

    public Watermark resize(int width, int height) {
        this.resizeWidth = width;
        this.resizeHeight = height;
        return this;
    }

    public BufferedImage applyWatermark(BufferedImage image, boolean copyright) {
        if (watermarkPath == null) {
            throw new IllegalStateException("Watermark path not set.");
        }

        try {
            BufferedImage watermarkImage = ImageIO.read(watermarkPath.toFile());
            BufferedImage resizedWatermark = resizeImage(watermarkImage, resizeWidth, resizeHeight);

            Graphics2D graphics = image.createGraphics();
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (copyright) {
                applyTiledWatermark(graphics, resizedWatermark, image.getWidth(), image.getHeight());
            } else {
                applySingleWatermark(graphics, resizedWatermark, image.getWidth(), image.getHeight());
            }

            graphics.dispose();
            return image;
        } catch (Exception e) {
            throw new RuntimeException("Failed to apply watermark.", e);
        }
    }

    private void applyTiledWatermark(Graphics2D graphics, BufferedImage watermarkImage, int imageWidth, int imageHeight) {
        int watermarkWidth = watermarkImage.getWidth();
        int watermarkHeight = watermarkImage.getHeight();

        for (int x = 0; x < imageWidth; x += watermarkWidth + spacing) {
            for (int y = 0; y < imageHeight; y += watermarkHeight + spacing) {
                graphics.drawImage(watermarkImage, x, y, null);
            }
        }
    }

    private void applySingleWatermark(Graphics2D graphics, BufferedImage watermarkImage, int imageWidth, int imageHeight) {
        int watermarkWidth = watermarkImage.getWidth();
        int watermarkHeight = watermarkImage.getHeight();

        int x = 0, y = 0;
        y = switch (position) {
            case TOP_LEFT -> {
                yield 0;
            }
            case TOP_RIGHT -> {
                x = imageWidth - watermarkWidth;
                yield 0;
            }
            case BOTTOM_LEFT -> {
                yield imageHeight - watermarkHeight;
            }
            case BOTTOM_RIGHT -> {
                x = imageWidth - watermarkWidth;
                yield imageHeight - watermarkHeight;
            }
            case CENTER -> {
                x = (imageWidth - watermarkWidth) / 2;
                yield (imageHeight - watermarkHeight) / 2;
            }
        };

        graphics.drawImage(watermarkImage, x, y, null);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    public static Watermark create() {
        return new Watermark();
    }
}