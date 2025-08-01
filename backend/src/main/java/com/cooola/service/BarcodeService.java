package com.cooola.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.oned.Code128Writer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BarcodeService {

    /**
     * QRコードを生成
     */
    public byte[] generateQRCode(String content, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }

            graphics.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();

        } catch (WriterException | IOException e) {
            log.error("Error generating QR code: {}", e.getMessage());
            throw new RuntimeException("QRコードの生成に失敗しました", e);
        }
    }

    /**
     * バーコード（Code128）を生成
     */
    public byte[] generateBarcode(String content, int width, int height) {
        try {
            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.CODE_128, width, height);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }

            graphics.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();

        } catch (WriterException | IOException e) {
            log.error("Error generating barcode: {}", e.getMessage());
            throw new RuntimeException("バーコードの生成に失敗しました", e);
        }
    }

    /**
     * 商品用QRコードを生成
     */
    public byte[] generateProductQRCode(String sku, String name) {
        String content = String.format("SKU:%s\nName:%s", sku, name);
        return generateQRCode(content, 200, 200);
    }

    /**
     * 商品用バーコードを生成
     */
    public byte[] generateProductBarcode(String sku) {
        return generateBarcode(sku, 300, 100);
    }
}