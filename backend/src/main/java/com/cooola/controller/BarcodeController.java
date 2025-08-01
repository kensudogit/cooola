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

@RestController
@RequestMapping("/barcode")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BarcodeController {

    private final BarcodeService barcodeService;
    private final ProductService productService;

    /**
     * QRコードを生成
     */
    @GetMapping("/qr")
    public ResponseEntity<byte[]> generateQRCode(
            @RequestParam String content,
            @RequestParam(defaultValue = "200") int width,
            @RequestParam(defaultValue = "200") int height) {

        try {
            byte[] qrCode = barcodeService.generateQRCode(content, width, height);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(qrCode.length);

            return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error generating QR code: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * バーコードを生成
     */
    @GetMapping("/code128")
    public ResponseEntity<byte[]> generateBarcode(
            @RequestParam String content,
            @RequestParam(defaultValue = "300") int width,
            @RequestParam(defaultValue = "100") int height) {

        try {
            byte[] barcode = barcodeService.generateBarcode(content, width, height);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(barcode.length);

            return new ResponseEntity<>(barcode, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error generating barcode: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 商品用QRコードを生成
     */
    @GetMapping("/product/qr/{sku}")
    public ResponseEntity<byte[]> generateProductQRCode(@PathVariable String sku) {
        try {
            return productService.getProductBySku(sku)
                    .map(product -> {
                        byte[] qrCode = barcodeService.generateProductQRCode(product.getSku(), product.getName());

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.IMAGE_PNG);
                        headers.setContentLength(qrCode.length);

                        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            log.error("Error generating product QR code: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 商品用バーコードを生成
     */
    @GetMapping("/product/barcode/{sku}")
    public ResponseEntity<byte[]> generateProductBarcode(@PathVariable String sku) {
        try {
            return productService.getProductBySku(sku)
                    .map(product -> {
                        byte[] barcode = barcodeService.generateProductBarcode(product.getSku());

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.IMAGE_PNG);
                        headers.setContentLength(barcode.length);

                        return new ResponseEntity<>(barcode, headers, HttpStatus.OK);
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            log.error("Error generating product barcode: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}