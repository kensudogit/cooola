# COOOLa クラウド型倉庫管理システム - Spring Boot バックエンド用 Dockerfile
# Java 17 + Spring Boot 3.x を使用したRESTful APIサーバーのコンテナ化設定

# ベースイメージ: OpenJDK 17 (スリム版)
# 軽量でセキュリティアップデートが適用された公式イメージ
FROM openjdk:17-jdk-slim

# 作業ディレクトリを設定
# コンテナ内でのアプリケーションの作業ディレクトリ
WORKDIR /app

# Gradle Wrapperをコピー
# ビルドツールの設定ファイルをコンテナに配置
COPY gradlew .
COPY gradle gradle
COPY build.gradle .

# Gradle Wrapperに実行権限を付与
# Linux環境でスクリプトを実行可能にする
RUN chmod +x ./gradlew

# 依存関係をダウンロード
# ビルド時間短縮のため、依存関係を事前にダウンロード
# --no-daemon: Gradleデーモンを無効化（コンテナ環境に最適化）
RUN ./gradlew dependencies --no-daemon

# ソースコードをコピー
# アプリケーションのソースコードをコンテナに配置
COPY src src

# アプリケーションをビルド
# テストをスキップしてアプリケーションをビルド
# -x test: テスト実行をスキップ
# --no-daemon: Gradleデーモンを無効化
RUN ./gradlew build -x test --no-daemon

# ポート8080を公開
# Spring Bootアプリケーションの標準ポート
EXPOSE 8080

# アプリケーションを起動
# Gradle Wrapperを使用してSpring Bootアプリケーションを起動
# --no-daemon: Gradleデーモンを無効化
CMD ["./gradlew", "bootRun", "--no-daemon"] 