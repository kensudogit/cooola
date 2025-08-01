# COOOLa - クラウド型倉庫管理システム

## 概要
COOOLaは、クラウドベースの包括的な倉庫管理システムです。在庫管理、入出庫管理、棚卸し、レポート機能などを提供します。

## 技術スタック

### バックエンド
- **Spring Boot 3.x** - メインアプリケーションフレームワーク
- **Java 17** - プログラミング言語
- **Gradle** - ビルドツール
- **Spring Data JPA** - データアクセス層
- **MySQL 8.0** - リレーショナルデータベース
- **JWT** - 認証・認可
- **AWS SDK** - クラウドサービス連携（S3、SQS）
- **Lombok** - ボイラープレートコード削減
- **ZXing** - バーコード・QRコード生成

### フロントエンド
- **AngularJS 1.8.x** - フロントエンドフレームワーク
- **Node.js 18** - JavaScript実行環境
- **npm** - パッケージマネージャー
- **Bootstrap 5** - CSSフレームワーク
- **jQuery** - JavaScriptライブラリ
- **Font Awesome** - アイコンフォント

### インフラストラクチャ
- **Docker** - コンテナ化
- **Docker Compose** - マルチコンテナ管理
- **AWS CloudFormation** - インフラストラクチャのコード化

### 監視・ログ
- **Redis** - キャッシュ・セッション管理
- **Elasticsearch** - ログ管理・検索
- **Kibana** - ログ可視化
- **Prometheus** - メトリクス収集
- **Grafana** - ダッシュボード

## 主要機能

### 商品管理
- 商品の登録・編集・削除
- カテゴリ管理
- バーコード・QRコード生成
- 商品検索・フィルタリング

### 在庫管理
- 在庫数量の追跡
- 在庫アラート
- 在庫履歴管理
- 棚卸し機能

### 入出庫管理
- 入庫処理
- 出庫処理
- 入出庫履歴
- 移動管理

### レポート機能
- 在庫レポート
- 入出庫レポート
- 売上レポート
- カスタムレポート

## 開発環境セットアップ

### 前提条件
- Docker Desktop
- Git

### 起動方法

#### 方法1: 改善版スクリプト（推奨）
```bash
start-improved.bat
```

#### 方法2: 基本スクリプト
```bash
start.bat
```

#### 方法3: 手動起動
```bash
docker-compose up -d
```

### アクセスURL
- **フロントエンド**: http://localhost:3002
- **バックエンドAPI**: http://localhost:8082/api
- **phpMyAdmin**: http://localhost:8081
- **Grafana**: http://localhost:3001 (admin/admin)
- **Kibana**: http://localhost:5601
- **Prometheus**: http://localhost:9090

## API エンドポイント

### 商品管理
- `GET /api/products` - 商品一覧取得
- `GET /api/products/{id}` - 商品詳細取得
- `POST /api/products` - 商品登録
- `PUT /api/products/{id}` - 商品更新
- `DELETE /api/products/{id}` - 商品削除

### バーコード・QRコード
- `GET /api/barcode/qr?content={content}&width={width}&height={height}` - QRコード生成
- `GET /api/barcode/code128?content={content}&width={width}&height={height}` - バーコード生成

## 管理スクリプト

### 起動・停止
- `start-improved.bat` - 改善版起動スクリプト（エラーハンドリング付き）
- `start.bat` - 基本起動スクリプト
- `stop.bat` - 停止スクリプト

### ログ・監視
- `logs.bat` - ログ表示スクリプト
- `status.bat` - 状態確認スクリプト

### メンテナンス
- `clean-rebuild.bat` - クリーンリビルドスクリプト

## プロジェクト構造

```
cooola/
├── backend/                 # Spring Boot バックエンド
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/cooola/
│   │   │   │       ├── controller/    # REST API コントローラー
│   │   │   │       ├── service/       # ビジネスロジック
│   │   │   │       ├── repository/    # データアクセス層
│   │   │   │       └── entity/        # JPA エンティティ
│   │   │   └── resources/
│   │   │       ├── application.yml    # アプリケーション設定
│   │   │       └── db/migration/      # データベースマイグレーション
│   │   └── test/                      # テストコード
│   ├── build.gradle                   # Gradle ビルド設定
│   └── Dockerfile                     # バックエンド用 Dockerfile
├── frontend/                # AngularJS フロントエンド
│   ├── js/                  # JavaScript ファイル
│   ├── css/                 # CSS ファイル
│   ├── index.html           # メイン HTML ファイル
│   ├── package.json         # npm 依存関係
│   └── Dockerfile           # フロントエンド用 Dockerfile
├── aws/                     # AWS CloudFormation テンプレート
│   └── cloudformation.yml   # インフラストラクチャ定義
├── monitoring/              # 監視設定
│   ├── prometheus.yml       # Prometheus 設定
│   └── grafana/             # Grafana 設定
├── docker-compose.yml       # Docker Compose 設定
├── start.bat               # Windows 起動スクリプト
├── start.sh                # Linux/Mac 起動スクリプト
└── README.md               # プロジェクト説明書
```

## トラブルシューティング

### よくある問題

#### 1. ポート競合エラー
**問題**: `Bind for 0.0.0.0:XXXX failed: port is already allocated`

**解決方法**:
- `docker-compose down` で既存のコンテナを停止
- または、`docker-compose.yml` でポート番号を変更

#### 2. ビルドエラー
**問題**: Java コンパイルエラーや npm インストールエラー

**解決方法**:
- `clean-rebuild.bat` を実行してクリーンリビルド
- または、`docker-compose build --no-cache` でキャッシュをクリア

#### 3. データベース接続エラー
**問題**: バックエンドがMySQLに接続できない

**解決方法**:
- MySQLコンテナが完全に起動するまで待機（約30秒）
- `docker-compose logs mysql` でログを確認

### ログの確認方法
```bash
# 全サービスのログ
docker-compose logs

# 特定サービスのログ
docker-compose logs backend
docker-compose logs frontend
docker-compose logs mysql

# リアルタイムログ
docker-compose logs -f
```

## ライセンス
MIT License

## 貢献
プルリクエストやイシューの報告を歓迎します。 
