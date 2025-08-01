package com.cooola.repository;

import com.cooola.entity.Product;
import com.cooola.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品リポジトリインターフェース
 * 
 * このインターフェースは商品エンティティのデータアクセス層を定義します。
 * Spring Data JPAを使用して、商品のCRUD操作やカスタムクエリを提供します。
 * 
 * @author COOOLa Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository  // Spring Bootリポジトリとして登録
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * SKUで商品を検索するメソッド
     * 
     * @param sku 商品のSKU（Stock Keeping Unit）
     * @return 商品情報（存在しない場合は空のOptional）
     */
    Optional<Product> findBySku(String sku);

    /**
     * バーコードで商品を検索するメソッド
     * 
     * @param barcode 商品のバーコード
     * @return 商品情報（存在しない場合は空のOptional）
     */
    Optional<Product> findByBarcode(String barcode);

    /**
     * SKUの存在チェックメソッド
     * 
     * @param sku チェック対象のSKU
     * @return SKUが存在する場合はtrue、存在しない場合はfalse
     */
    boolean existsBySku(String sku);

    /**
     * バーコードの存在チェックメソッド
     * 
     * @param barcode チェック対象のバーコード
     * @return バーコードが存在する場合はtrue、存在しない場合はfalse
     */
    boolean existsByBarcode(String barcode);

    /**
     * カテゴリで商品を検索するメソッド
     * 
     * @param category 商品カテゴリ
     * @return 指定カテゴリの商品一覧
     */
    List<Product> findByCategory(ProductCategory category);

    /**
     * アクティブ状態で商品を検索するメソッド
     * 
     * @param isActive アクティブ状態（true: アクティブ、false: 非アクティブ）
     * @return 指定状態の商品一覧
     */
    List<Product> findByIsActive(Boolean isActive);

    /**
     * アクティブ状態で商品を検索するメソッド（ページネーション付き）
     * 
     * @param isActive アクティブ状態（true: アクティブ、false: 非アクティブ）
     * @param pageable ページネーション情報
     * @return ページネーションされた商品一覧
     */
    Page<Product> findByIsActive(Boolean isActive, Pageable pageable);

    /**
     * キーワードで商品を検索するメソッド
     * 
     * SKU、商品名、説明文、バーコードで部分一致検索を行います。
     * 
     * @param keyword 検索キーワード
     * @return 検索結果の商品一覧
     */
    @Query("SELECT p FROM Product p WHERE p.sku LIKE %:keyword% OR p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR p.barcode LIKE %:keyword%")
    List<Product> findByKeyword(@Param("keyword") String keyword);

    /**
     * キーワードで商品を検索するメソッド（ページネーション付き）
     * 
     * SKU、商品名、説明文、バーコードで部分一致検索を行います。
     * 
     * @param keyword 検索キーワード
     * @param pageable ページネーション情報
     * @return ページネーションされた検索結果
     */
    @Query("SELECT p FROM Product p WHERE p.sku LIKE %:keyword% OR p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR p.barcode LIKE %:keyword%")
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * カテゴリIDで商品を検索するメソッド
     * 
     * @param categoryId カテゴリID
     * @return 指定カテゴリの商品一覧
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * カテゴリ別商品数を取得するメソッド
     * 
     * @param categoryId カテゴリID
     * @return 指定カテゴリの商品数
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * アクティブな商品をSKUで部分一致検索するメソッド
     * 
     * @param sku SKUの部分文字列
     * @return アクティブな商品の一覧
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.sku LIKE %:sku%")
    List<Product> findActiveBySkuContaining(@Param("sku") String sku);
}