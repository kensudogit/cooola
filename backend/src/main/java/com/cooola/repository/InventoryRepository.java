package com.cooola.repository;

import com.cooola.entity.Inventory;
import com.cooola.entity.Product;
import com.cooola.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByProduct(Product product);

    List<Inventory> findByWarehouse(Warehouse warehouse);

    List<Inventory> findByProductAndWarehouse(Product product, Warehouse warehouse);

    Optional<Inventory> findByProductAndWarehouseAndLocation(Product product, Warehouse warehouse, Long locationId);

    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.warehouse.id = :warehouseId")
    List<Inventory> findByProductIdAndWarehouseId(@Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId);

    @Query("SELECT i FROM Inventory i WHERE i.warehouse.id = :warehouseId")
    List<Inventory> findByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId")
    List<Inventory> findByProductId(@Param("productId") Long productId);

    @Query("SELECT i FROM Inventory i WHERE i.availableQuantity <= :threshold")
    List<Inventory> findLowStock(@Param("threshold") BigDecimal threshold);

    @Query("SELECT i FROM Inventory i WHERE i.expiryDate <= :date")
    List<Inventory> findExpiringSoon(@Param("date") LocalDate date);

    @Query("SELECT i FROM Inventory i WHERE i.quantity > 0")
    List<Inventory> findInStock();

    @Query("SELECT i FROM Inventory i WHERE i.quantity = 0")
    List<Inventory> findOutOfStock();

    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.product.id = :productId")
    BigDecimal getTotalQuantityByProductId(@Param("productId") Long productId);

    @Query("SELECT SUM(i.quantity * i.unitCost) FROM Inventory i WHERE i.warehouse.id = :warehouseId")
    BigDecimal getTotalValueByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query("SELECT i FROM Inventory i WHERE i.warehouse.id = :warehouseId")
    Page<Inventory> findByWarehouseId(@Param("warehouseId") Long warehouseId, Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE i.product.sku LIKE %:sku% OR i.product.name LIKE %:name%")
    List<Inventory> findByProductSkuOrName(@Param("sku") String sku, @Param("name") String name);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.warehouse.id = :warehouseId AND i.quantity > 0")
    long countInStockByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.warehouse.id = :warehouseId AND i.quantity = 0")
    long countOutOfStockByWarehouseId(@Param("warehouseId") Long warehouseId);
}