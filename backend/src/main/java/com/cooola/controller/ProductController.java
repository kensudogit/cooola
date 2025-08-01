package com.cooola.controller;

import com.cooola.entity.Product;
import com.cooola.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    /**
     * 商品一覧を取得
     */
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(products);
    }

    /**
     * 商品をIDで取得
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 商品をSKUで取得
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {
        return productService.getProductBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 商品をバーコードで取得
     */
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Product> getProductByBarcode(@PathVariable String barcode) {
        return productService.getProductByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * キーワードで商品を検索
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * カテゴリ別商品一覧を取得
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * 商品を新規作成
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.ok(createdProduct);
        } catch (IllegalArgumentException e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 商品を更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            log.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 商品を削除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 商品数を取得
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getProductCount() {
        long count = productService.getProductCount();
        return ResponseEntity.ok(count);
    }

    /**
     * アクティブな商品数を取得
     */
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveProductCount() {
        long count = productService.getActiveProductCount();
        return ResponseEntity.ok(count);
    }

    /**
     * カテゴリ別商品数を取得
     */
    @GetMapping("/count/category/{categoryId}")
    public ResponseEntity<Long> getProductCountByCategory(@PathVariable Long categoryId) {
        long count = productService.getProductCountByCategory(categoryId);
        return ResponseEntity.ok(count);
    }
}