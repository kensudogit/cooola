# Spring Boot バックエンド用 Dockerfile
FROM openjdk:17-jdk-slim

# 作業ディレクトリを設定
WORKDIR /app

# Gradle Wrapperをコピー
COPY gradlew .
COPY gradle gradle
COPY build.gradle .

# Gradle Wrapperに実行権限を付与
RUN chmod +x ./gradlew

# 依存関係をダウンロード
RUN ./gradlew dependencies --no-daemon

# ソースコードをコピー
COPY src src

# アプリケーションをビルド
RUN ./gradlew build -x test --no-daemon

# ポート8080を公開
EXPOSE 8080

# アプリケーションを起動
CMD ["./gradlew", "bootRun", "--no-daemon"] 