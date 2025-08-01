package com.cooola.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品エンティティクラス
 * 
 * このクラスは商品情報を表すJPAエンティティです。
 * 商品の基本情報、カテゴリ、物理的特性、バーコード情報などを管理します。
 * 
 * @author COOOLa Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity                    // JPAエンティティとして登録
@Table(name = "products")  // テーブル名を指定
@Data                      // Lombok: getter/setter/toString/equals/hashCodeを自動生成
@NoArgsConstructor         // Lombok: 引数なしコンストラクタを自動生成
@AllArgsConstructor        // Lombok: 全引数コンストラクタを自動生成
public class Product {

    /**
     * 商品ID（主キー）
     * 自動採番される一意の識別子
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自動採番（AUTO_INCREMENT）
    private Long id;

    /**
     * 商品のSKU（Stock Keeping Unit）
     * 商品管理のための一意の商品コード
     */
    @Column(unique = true, nullable = false, length = 50)  // 一意制約、NOT NULL、最大50文字
    private String sku;

    /**
     * 商品名
     * 商品の表示名
     */
    @Column(nullable = false, length = 200)  // NOT NULL、最大200文字
    private String name;

    /**
     * 商品説明
     * 商品の詳細説明文
     */
    @Column(columnDefinition = "TEXT")  // TEXT型で格納（長文対応）
    private String description;

    /**
     * 商品カテゴリ
     * 商品の分類カテゴリ（多対一の関係）
     */
    @ManyToOne(fetch = FetchType.LAZY)  // 多対一の関係、遅延読み込み
    @JoinColumn(name = "category_id", nullable = false)  // 外部キー、NOT NULL
    private ProductCategory category;

    /**
     * 単位
     * 商品の計量単位（個、kg、m等）
     */
    @Column(name = "unit_of_measure", nullable = false, length = 20)  // NOT NULL、最大20文字
    private String unitOfMeasure;

    /**
     * 重量
     * 商品の重量（小数点3桁まで）
     */
    @Column(precision = 10, scale = 3)  // 全体10桁、小数点3桁
    private BigDecimal weight;

    /**
     * 寸法
     * 商品のサイズ情報（例：100x200x300mm）
     */
    @Column(length = 50)  // 最大50文字
    private String dimensions;

    /**
     * バーコード
     * 商品のバーコード番号
     */
    @Column(length = 100)  // 最大100文字
    private String barcode;

    /**
     * アクティブ状態
     * 商品の有効/無効状態（論理削除用）
     */
    @Column(name = "is_active", nullable = false)  // NOT NULL
    private Boolean isActive = true;  // デフォルトはtrue（アクティブ）

    /**
     * 作成日時
     * レコードの作成日時（自動設定、更新不可）
     */
    @CreationTimestamp  // 作成時に自動設定
    @Column(name = "created_at", updatable = false)  // 更新不可
    private LocalDateTime createdAt;

    /**
     * 更新日時
     * レコードの最終更新日時（自動更新）
     */
    @UpdateTimestamp  // 更新時に自動設定
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}