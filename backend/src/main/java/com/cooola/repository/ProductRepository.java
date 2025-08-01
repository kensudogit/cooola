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

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    Optional<Product> findByBarcode(String barcode);

    boolean existsBySku(String sku);

    boolean existsByBarcode(String barcode);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByIsActive(Boolean isActive);

    Page<Product> findByIsActive(Boolean isActive, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.sku LIKE %:keyword% OR p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR p.barcode LIKE %:keyword%")
    List<Product> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.sku LIKE %:keyword% OR p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR p.barcode LIKE %:keyword%")
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.sku LIKE %:sku%")
    List<Product> findActiveBySkuContaining(@Param("sku") String sku);
}