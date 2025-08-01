package com.cooola.repository;

import com.cooola.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findByParentIsNull();

    List<ProductCategory> findByParent(ProductCategory parent);

    List<ProductCategory> findByIsActive(Boolean isActive);

    @Query("SELECT c FROM ProductCategory c WHERE c.parent.id = :parentId")
    List<ProductCategory> findByParentId(@Param("parentId") Long parentId);

    @Query("SELECT c FROM ProductCategory c WHERE c.name LIKE %:keyword% OR c.description LIKE %:keyword%")
    List<ProductCategory> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(c) FROM ProductCategory c WHERE c.parent.id = :parentId")
    long countByParentId(@Param("parentId") Long parentId);
}