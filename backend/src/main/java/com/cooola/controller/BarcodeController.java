package com.cooola.controller;

import com.cooola.service.BarcodeService;
import com.cooola.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * バーコード・QRコード生成コントローラー
 * 
 * このクラスはバーコードとQRコードの生成に関するRESTful APIエンドポイントを提供します。
 * 商品管理システムで使用するバーコード・QRコードの動的生成機能を実装しています。
 * 
 * @author COOOLa Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@RestController                    // RESTful APIコントローラー
@RequestMapping("/barcode")        // ベースパス: /api/barcode
@RequiredArgsConstructor           // コンストラクタインジェクション
@Slf4j                            // ログ機能
@CrossOrigin(origins = "*")        // CORS設定（全オリジン許可）
public class BarcodeController {

    // バーコード・QRコード生成サービス
    private final BarcodeService barcodeService;
    // 商品管理サービス
    private final ProductService productService;

    /**
     * QRコードを生成するエンドポイント
     * 
     * 指定された内容でQRコードを生成し、PNG画像として返却します。
     * 
     * @param content QRコードに含める内容（テキスト、URL等）
     * @param width QRコードの幅（デフォルト: 200px）
     * @param height QRコードの高さ（デフォルト: 200px）
     * @return QRコードのPNG画像データ
     */
    @GetMapping("/qr")
    public ResponseEntity<byte[]> generateQRCode(
            @RequestParam String content,
            @RequestParam(defaultValue = "200") int width,
            @RequestParam(defaultValue = "200") int height) {

        try {
            // QRコードを生成
            byte[] qrCode = barcodeService.generateQRCode(content, width, height);

            // HTTPレスポンスヘッダーを設定
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);  // PNG画像として設定
            headers.setContentLength(qrCode.length);      // コンテンツ長を設定

            return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);

        } catch (Exception e) {
            // エラーログを出力
            log.error("Error generating QR code: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * バーコード（Code128）を生成するエンドポイント
     * 
     * 指定された内容でCode128形式のバーコードを生成し、PNG画像として返却します。
     * 
     * @param content バーコードに含める内容（商品コード、SKU等）
     * @param width バーコードの幅（デフォルト: 300px）
     * @param height バーコードの高さ（デフォルト: 100px）
     * @return バーコードのPNG画像データ
     */
    @GetMapping("/code128")
    public ResponseEntity<byte[]> generateBarcode(
            @RequestParam String content,
            @RequestParam(defaultValue = "300") int width,
            @RequestParam(defaultValue = "100") int height) {

        try {
            // バーコードを生成
            byte[] barcode = barcodeService.generateBarcode(content, width, height);

            // HTTPレスポンスヘッダーを設定
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);  // PNG画像として設定
            headers.setContentLength(barcode.length);     // コンテンツ長を設定

            return new ResponseEntity<>(barcode, headers, HttpStatus.OK);

        } catch (Exception e) {
            // エラーログを出力
            log.error("Error generating barcode: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 商品用QRコードを生成するエンドポイント
     * 
     * 指定されたSKUの商品情報を含むQRコードを生成します。
     * 商品の詳細情報（SKU、商品名等）がQRコードに含まれます。
     * 
     * @param sku 商品のSKU（Stock Keeping Unit）
     * @return 商品情報を含むQRコードのPNG画像データ
     */
    @GetMapping("/product/qr/{sku}")
    public ResponseEntity<byte[]> generateProductQRCode(@PathVariable String sku) {
        try {
            // SKUで商品を検索し、存在する場合はQRコードを生成
            return productService.getProductBySku(sku)
                    .map(product -> {
                        // 商品情報を含むQRコードを生成
                        byte[] qrCode = barcodeService.generateProductQRCode(product.getSku(), product.getName());

                        // HTTPレスポンスヘッダーを設定
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.IMAGE_PNG);  // PNG画像として設定
                        headers.setContentLength(qrCode.length);      // コンテンツ長を設定

                        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
                    })
                    .orElse(ResponseEntity.notFound().build());  // 商品が見つからない場合は404

        } catch (Exception e) {
            // エラーログを出力
            log.error("Error generating product QR code: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 商品用バーコードを生成するエンドポイント
     * 
     * 指定されたSKUの商品用バーコードを生成します。
     * 商品のSKUがバーコードに含まれ、スキャン時に商品を識別できます。
     * 
     * @param sku 商品のSKU（Stock Keeping Unit）
     * @return 商品用バーコードのPNG画像データ
     */
    @GetMapping("/product/barcode/{sku}")
    public ResponseEntity<byte[]> generateProductBarcode(@PathVariable String sku) {
        try {
            // SKUで商品を検索し、存在する場合はバーコードを生成
            return productService.getProductBySku(sku)
                    .map(product -> {
                        // 商品用バーコードを生成
                        byte[] barcode = barcodeService.generateProductBarcode(product.getSku());

                        // HTTPレスポンスヘッダーを設定
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.IMAGE_PNG);  // PNG画像として設定
                        headers.setContentLength(barcode.length);     // コンテンツ長を設定

                        return new ResponseEntity<>(barcode, headers, HttpStatus.OK);
                    })
                    .orElse(ResponseEntity.notFound().build());  // 商品が見つからない場合は404

        } catch (Exception e) {
            // エラーログを出力
            log.error("Error generating product barcode: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}