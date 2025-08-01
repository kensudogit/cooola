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

/**
 * バーコード・QRコード生成サービス
 * 
 * このクラスはバーコードとQRコードの生成機能を提供します。
 * ZXingライブラリを使用して、高品質なバーコード・QRコード画像を生成します。
 * 
 * @author COOOLa Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service  // Spring Bootサービスとして登録
@Slf4j    // ログ機能
public class BarcodeService {

    /**
     * QRコードを生成するメソッド
     * 
     * 指定された内容、幅、高さでQRコードを生成し、PNG画像のバイト配列として返却します。
     * 
     * @param content QRコードに含める内容（テキスト、URL等）
     * @param width QRコードの幅（ピクセル）
     * @param height QRコードの高さ（ピクセル）
     * @return QRコードのPNG画像データ
     * @throws RuntimeException QRコード生成に失敗した場合
     */
    public byte[] generateQRCode(String content, int width, int height) {
        try {
            // QRコードライターを初期化
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            
            // エンコードヒントを設定
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  // 文字エンコーディング
            hints.put(EncodeHintType.MARGIN, 1);               // マージン設定

            // QRコードのビットマトリックスを生成
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 画像を生成
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            
            // 背景を白で塗りつぶし
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            
            // QRコード部分を黒で描画
            graphics.setColor(Color.BLACK);

            // ビットマトリックスを画像に変換
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }

            // グラフィックスリソースを解放
            graphics.dispose();

            // PNG画像としてバイト配列に変換
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();

        } catch (WriterException | IOException e) {
            // エラーログを出力
            log.error("Error generating QR code: {}", e.getMessage());
            throw new RuntimeException("QRコードの生成に失敗しました", e);
        }
    }

    /**
     * バーコード（Code128）を生成するメソッド
     * 
     * 指定された内容、幅、高さでCode128形式のバーコードを生成し、PNG画像のバイト配列として返却します。
     * 
     * @param content バーコードに含める内容（商品コード、SKU等）
     * @param width バーコードの幅（ピクセル）
     * @param height バーコードの高さ（ピクセル）
     * @return バーコードのPNG画像データ
     * @throws RuntimeException バーコード生成に失敗した場合
     */
    public byte[] generateBarcode(String content, int width, int height) {
        try {
            // Code128バーコードライターを初期化
            Code128Writer barcodeWriter = new Code128Writer();
            
            // バーコードのビットマトリックスを生成
            BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.CODE_128, width, height);

            // 画像を生成
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            
            // 背景を白で塗りつぶし
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            
            // バーコード部分を黒で描画
            graphics.setColor(Color.BLACK);

            // ビットマトリックスを画像に変換
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }

            // グラフィックスリソースを解放
            graphics.dispose();

            // PNG画像としてバイト配列に変換
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();

        } catch (WriterException | IOException e) {
            // エラーログを出力
            log.error("Error generating barcode: {}", e.getMessage());
            throw new RuntimeException("バーコードの生成に失敗しました", e);
        }
    }

    /**
     * 商品用QRコードを生成するメソッド
     * 
     * 商品のSKUと名前を含むQRコードを生成します。
     * 商品情報が構造化された形式でQRコードに含まれます。
     * 
     * @param sku 商品のSKU（Stock Keeping Unit）
     * @param name 商品名
     * @return 商品情報を含むQRコードのPNG画像データ
     */
    public byte[] generateProductQRCode(String sku, String name) {
        // 商品情報を構造化された形式でQRコードに含める
        String content = String.format("SKU:%s\nName:%s", sku, name);
        return generateQRCode(content, 200, 200);  // 200x200ピクセルのQRコードを生成
    }

    /**
     * 商品用バーコードを生成するメソッド
     * 
     * 商品のSKUをバーコードとして生成します。
     * スキャン時に商品を識別できるようになります。
     * 
     * @param sku 商品のSKU（Stock Keeping Unit）
     * @return 商品用バーコードのPNG画像データ
     */
    public byte[] generateProductBarcode(String sku) {
        return generateBarcode(sku, 300, 100);  // 300x100ピクセルのバーコードを生成
    }
}