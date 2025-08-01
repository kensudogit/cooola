package com.cooola.service;

import com.cooola.entity.Product;
import com.cooola.entity.ProductCategory;
import com.cooola.repository.ProductRepository;
import com.cooola.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 商品管理サービス
 * 
 * このクラスは商品管理に関するビジネスロジックを提供します。
 * 商品のCRUD操作、検索、統計情報の取得などの機能を実装しています。
 * データの整合性チェックやバリデーションも含まれています。
 * 
 * @author COOOLa Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service                    // Spring Bootサービスとして登録
@RequiredArgsConstructor    // コンストラクタインジェクション
@Slf4j                     // ログ機能
@Transactional             // トランザクション管理
public class ProductService {

    // 商品リポジトリ
    private final ProductRepository productRepository;
    // 商品カテゴリリポジトリ
    private final ProductCategoryRepository categoryRepository;

    /**
     * 商品一覧を取得するメソッド
     * 
     * アクティブな商品のみをページネーション付きで取得します。
     * 
     * @param pageable ページネーション情報
     * @return ページネーションされた商品一覧
     */
    @Transactional(readOnly = true)  // 読み取り専用トランザクション
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findByIsActive(true, pageable);
    }

    /**
     * 商品をIDで取得するメソッド
     * 
     * @param id 商品ID
     * @return 商品情報（存在しない場合は空のOptional）
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 商品をSKUで取得するメソッド
     * 
     * @param sku 商品のSKU（Stock Keeping Unit）
     * @return 商品情報（存在しない場合は空のOptional）
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    /**
     * 商品をバーコードで取得するメソッド
     * 
     * @param barcode 商品のバーコード
     * @return 商品情報（存在しない場合は空のOptional）
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    /**
     * キーワードで商品を検索するメソッド
     * 
     * 商品名、説明文などで部分一致検索を行います。
     * 
     * @param keyword 検索キーワード
     * @return 検索結果の商品一覧
     */
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByKeyword(keyword);
    }

    /**
     * カテゴリ別商品一覧を取得するメソッド
     * 
     * @param categoryId カテゴリID
     * @return 指定カテゴリの商品一覧
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * 商品を新規作成するメソッド
     * 
     * 商品の重複チェック、カテゴリの存在チェックを行い、新しい商品を作成します。
     * 
     * @param product 作成する商品情報
     * @return 作成された商品情報
     * @throws IllegalArgumentException SKU重複、バーコード重複、カテゴリ不存在の場合
     */
    public Product createProduct(Product product) {
        // SKUの重複チェック
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("SKU already exists: " + product.getSku());
        }

        // バーコードの重複チェック
        if (product.getBarcode() != null && productRepository.existsByBarcode(product.getBarcode())) {
            throw new IllegalArgumentException("Barcode already exists: " + product.getBarcode());
        }

        // カテゴリの存在チェック
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Optional<ProductCategory> category = categoryRepository.findById(product.getCategory().getId());
            if (category.isEmpty()) {
                throw new IllegalArgumentException("Category not found: " + product.getCategory().getId());
            }
            product.setCategory(category.get());
        }

        // 商品をアクティブ状態で保存
        product.setIsActive(true);
        Product savedProduct = productRepository.save(product);
        log.info("Product created: {}", savedProduct.getSku());
        return savedProduct;
    }

    /**
     * 商品を更新するメソッド
     * 
     * 既存の商品情報を更新します。重複チェックやカテゴリの存在チェックも行います。
     * 
     * @param id 更新対象の商品ID
     * @param productDetails 更新する商品情報
     * @return 更新された商品情報
     * @throws IllegalArgumentException 商品不存在、SKU重複、バーコード重複、カテゴリ不存在の場合
     */
    public Product updateProduct(Long id, Product productDetails) {
        // 更新対象の商品を取得
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        // SKUの重複チェック（自分以外）
        if (!product.getSku().equals(productDetails.getSku()) &&
                productRepository.existsBySku(productDetails.getSku())) {
            throw new IllegalArgumentException("SKU already exists: " + productDetails.getSku());
        }

        // バーコードの重複チェック（自分以外）
        if (productDetails.getBarcode() != null &&
                !productDetails.getBarcode().equals(product.getBarcode()) &&
                productRepository.existsByBarcode(productDetails.getBarcode())) {
            throw new IllegalArgumentException("Barcode already exists: " + productDetails.getBarcode());
        }

        // カテゴリの存在チェック
        if (productDetails.getCategory() != null && productDetails.getCategory().getId() != null) {
            Optional<ProductCategory> category = categoryRepository.findById(productDetails.getCategory().getId());
            if (category.isEmpty()) {
                throw new IllegalArgumentException("Category not found: " + productDetails.getCategory().getId());
            }
            product.setCategory(category.get());
        }

        // 商品情報を更新
        product.setSku(productDetails.getSku());
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setUnitOfMeasure(productDetails.getUnitOfMeasure());
        product.setWeight(productDetails.getWeight());
        product.setDimensions(productDetails.getDimensions());
        product.setBarcode(productDetails.getBarcode());

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated: {}", updatedProduct.getSku());
        return updatedProduct;
    }

    /**
     * 商品を削除するメソッド（論理削除）
     * 
     * 商品を物理削除せず、isActiveフラグをfalseに設定して論理削除します。
     * 
     * @param id 削除対象の商品ID
     * @throws IllegalArgumentException 商品が存在しない場合
     */
    public void deleteProduct(Long id) {
        // 削除対象の商品を取得
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        // 論理削除（isActiveをfalseに設定）
        product.setIsActive(false);
        productRepository.save(product);
        log.info("Product deleted: {}", product.getSku());
    }

    /**
     * 商品数を取得するメソッド
     * 
     * @return 全商品数（削除済み含む）
     */
    @Transactional(readOnly = true)
    public long getProductCount() {
        return productRepository.count();
    }

    /**
     * アクティブな商品数を取得するメソッド
     * 
     * @return アクティブな商品数（論理削除されていない商品数）
     */
    @Transactional(readOnly = true)
    public long getActiveProductCount() {
        return productRepository.countByIsActive(true);
    }

    /**
     * カテゴリ別商品数を取得するメソッド
     * 
     * @param categoryId カテゴリID
     * @return 指定カテゴリの商品数
     */
    @Transactional(readOnly = true)
    public long getProductCountByCategory(Long categoryId) {
        return productRepository.countByCategoryId(categoryId);
    }
}