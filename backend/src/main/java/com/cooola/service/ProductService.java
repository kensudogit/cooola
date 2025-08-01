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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    /**
     * 商品一覧を取得
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findByIsActive(true, pageable);
    }

    /**
     * 商品をIDで取得
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 商品をSKUで取得
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    /**
     * 商品をバーコードで取得
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    /**
     * キーワードで商品を検索
     */
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByKeyword(keyword);
    }

    /**
     * カテゴリ別商品一覧を取得
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * 商品を新規作成
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

        product.setIsActive(true);
        Product savedProduct = productRepository.save(product);
        log.info("Product created: {}", savedProduct.getSku());
        return savedProduct;
    }

    /**
     * 商品を更新
     */
    public Product updateProduct(Long id, Product productDetails) {
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
     * 商品を削除（論理削除）
     */
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.setIsActive(false);
        productRepository.save(product);
        log.info("Product deleted: {}", product.getSku());
    }

    /**
     * 商品数を取得
     */
    @Transactional(readOnly = true)
    public long getProductCount() {
        return productRepository.count();
    }

    /**
     * アクティブな商品数を取得
     */
    @Transactional(readOnly = true)
    public long getActiveProductCount() {
        return productRepository.countByIsActive(true);
    }

    /**
     * カテゴリ別商品数を取得
     */
    @Transactional(readOnly = true)
    public long getProductCountByCategory(Long categoryId) {
        return productRepository.countByCategoryId(categoryId);
    }
}