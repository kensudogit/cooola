package com.cooola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * COOOLa - クラウド型倉庫管理システム
 * メインアプリケーションクラス
 * 
 * このクラスはSpring Bootアプリケーションのエントリーポイントです。
 * アプリケーションの起動と設定を行います。
 * 
 * @author COOOLa Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootApplication  // Spring Bootアプリケーションの自動設定を有効化
@EnableScheduling       // スケジュール機能を有効化（定期タスク実行用）
public class CooolaApplication {

    /**
     * アプリケーションのメインメソッド
     * Spring Bootアプリケーションを起動します
     * 
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        // Spring Bootアプリケーションを起動
        SpringApplication.run(CooolaApplication.class, args);
    }
}