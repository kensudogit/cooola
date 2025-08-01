package com.cooola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * COOOLa - クラウド型倉庫管理システム
 * メインアプリケーションクラス
 */
@SpringBootApplication
@EnableScheduling
public class CooolaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CooolaApplication.class, args);
    }
}