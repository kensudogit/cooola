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

/**
 * 商品管理コントローラー
 * 
 * このクラスは商品管理に関するRESTful APIエンドポイントを提供します。
 * 商品のCRUD操作、検索、統計情報の取得などの機能を実装しています。
 * 
 * @author COOOLa Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@RestController                    // RESTful APIコントローラー
@RequestMapping("/products")       // ベースパス: /api/products
@RequiredArgsConstructor           // コンストラクタインジェクション
@Slf4j                            // ログ機能
@CrossOrigin(origins = "*")        // CORS設定（全オリジン許可）
public class ProductController {

    // 商品管理サービス
    private final ProductService productService;

    /**
     * 商品一覧を取得するエンドポイント
     * 
     * ページネーション、ソート機能付きで商品一覧を取得します。
     * 
     * @param page ページ番号（デフォルト: 0）
     * @param size 1ページあたりの件数（デフォルト: 20）
     * @param sortBy ソート項目（デフォルト: id）
     * @param sortDir ソート方向（asc/desc、デフォルト: desc）
     * @return ページネーションされた商品一覧
     */
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        // ソート条件を設定
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        // ページネーション条件を設定
        Pageable pageable = PageRequest.of(page, size, sort);
        // 商品一覧を取得
        Page<Product> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(products);
    }

    /**
     * 商品をIDで取得するエンドポイント
     * 
     * @param id 商品ID
     * @return 商品情報（存在しない場合は404）
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 商品をSKUで取得するエンドポイント
     * 
     * @param sku 商品のSKU（Stock Keeping Unit）
     * @return 商品情報（存在しない場合は404）
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {
        return productService.getProductBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 商品をバーコードで取得するエンドポイント
     * 
     * @param barcode 商品のバーコード
     * @return 商品情報（存在しない場合は404）
     */
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Product> getProductByBarcode(@PathVariable String barcode) {
        return productService.getProductByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * キーワードで商品を検索するエンドポイント
     * 
     * 商品名、説明文などで部分一致検索を行います。
     * 
     * @param keyword 検索キーワード
     * @return 検索結果の商品一覧
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * カテゴリ別商品一覧を取得するエンドポイント
     * 
     * @param categoryId カテゴリID
     * @return 指定カテゴリの商品一覧
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * 商品を新規作成するエンドポイント
     * 
     * @param product 作成する商品情報
     * @return 作成された商品情報
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.ok(createdProduct);
        } catch (IllegalArgumentException e) {
            // バリデーションエラーのログ出力
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 商品を更新するエンドポイント
     * 
     * @param id 更新対象の商品ID
     * @param product 更新する商品情報
     * @return 更新された商品情報
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            // バリデーションエラーのログ出力
            log.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 商品を削除するエンドポイント
     * 
     * @param id 削除対象の商品ID
     * @return 削除成功時は200、商品が存在しない場合は404
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // 商品が存在しない場合のログ出力
            log.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 商品数を取得するエンドポイント
     * 
     * @return 全商品数
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getProductCount() {
        long count = productService.getProductCount();
        return ResponseEntity.ok(count);
    }

    /**
     * アクティブな商品数を取得するエンドポイント
     * 
     * @return アクティブな商品数
     */
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveProductCount() {
        long count = productService.getActiveProductCount();
        return ResponseEntity.ok(count);
    }

    /**
     * カテゴリ別商品数を取得するエンドポイント
     * 
     * @param categoryId カテゴリID
     * @return 指定カテゴリの商品数
     */
    @GetMapping("/count/category/{categoryId}")
    public ResponseEntity<Long> getProductCountByCategory(@PathVariable Long categoryId) {
        long count = productService.getProductCountByCategory(categoryId);
        return ResponseEntity.ok(count);
    }
}