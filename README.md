# COOOLa - クラウド型倉庫管理システム

## 概要
COOOLaは、クラウドベースの包括的な倉庫管理システムです。在庫管理、入出庫管理、棚卸し、レポート機能などを提供します。

## 技術スタック
- **バックエンド**: Spring Boot 3.x + Java 17
- **フロントエンド**: AngularJS 1.8.x
- **データベース**: MySQL 8.0
- **パッケージ管理**: Gradle
- **クラウド**: AWS (ECS, RDS, S3, CloudFront)

## 主要機能

### 🔧 マスタ管理
- **商品マスタ**: SKU、商品名、カテゴリ、単位、重量、サイズ、バーコード管理
- **倉庫マスタ**: 倉庫情報、連絡先、住所管理
- **ロケーション管理**: 棚、エリア、ビン単位の在庫配置管理
- **カテゴリ管理**: 階層構造の商品カテゴリ管理
- **ユーザー管理**: ロールベースのアクセス制御（ADMIN/MANAGER/OPERATOR）

### 📦 在庫管理
- **在庫一覧**: リアルタイム在庫状況の表示
- **在庫検索**: SKU、商品名、バーコードでの高速検索
- **在庫移動**: ロケーション間の在庫移動履歴管理
- **在庫調整**: 棚卸しによる在庫数量の調整
- **在庫アラート**: 在庫不足、期限切れ商品の自動通知

### 🚚 入出庫管理
- **入庫管理**: 仕入先からの商品入庫処理
- **出庫管理**: 顧客への商品出庫処理
- **入出庫履歴**: 詳細な入出庫履歴の追跡
- **バッチ処理**: 大量データの一括入出庫処理

### 📊 レポート・分析
- **在庫レポート**: 倉庫別、カテゴリ別在庫レポート
- **入出庫レポート**: 日次・月次入出庫レポート
- **ABC分析**: 商品の重要度分析
- **在庫回転率**: 商品別在庫回転率の算出
- **ダッシュボード**: リアルタイムKPI表示

### 🏷️ バーコード・QRコード
- **バーコード生成**: Code128形式のバーコード自動生成
- **QRコード生成**: 商品情報を含むQRコード生成
- **バーコード印刷**: ラベル印刷機能
- **スキャン対応**: バーコードスキャナーでの入出庫処理

### 🔍 検索・フィルター
- **全文検索**: 商品名、説明文での全文検索
- **高度フィルター**: カテゴリ、倉庫、在庫状況での絞り込み
- **履歴検索**: 過去の入出庫履歴の検索
- **エクスポート**: CSV/Excel形式でのデータエクスポート

## 開発環境セットアップ

### 前提条件
- Java 17+
- Node.js 18+
- Docker & Docker Compose
- MySQL 8.0

### API エンドポイント

#### 商品管理
```
GET    /api/products              # 商品一覧取得
GET    /api/products/{id}         # 商品詳細取得
GET    /api/products/sku/{sku}    # SKUで商品取得
GET    /api/products/barcode/{barcode} # バーコードで商品取得
GET    /api/products/search       # 商品検索
POST   /api/products              # 商品作成
PUT    /api/products/{id}         # 商品更新
DELETE /api/products/{id}         # 商品削除
```

#### 在庫管理
```
GET    /api/inventory             # 在庫一覧取得
GET    /api/inventory/{id}        # 在庫詳細取得
GET    /api/inventory/low-stock   # 在庫不足商品取得
GET    /api/inventory/expiring    # 期限切れ商品取得
POST   /api/inventory/movement    # 在庫移動
PUT    /api/inventory/{id}        # 在庫更新
```

#### バーコード・QRコード
```
GET    /api/barcode/qr            # QRコード生成
GET    /api/barcode/code128       # バーコード生成
GET    /api/barcode/product/qr/{sku}    # 商品用QRコード
GET    /api/barcode/product/barcode/{sku} # 商品用バーコード
```

#### レポート
```
GET    /api/reports/inventory     # 在庫レポート
GET    /api/reports/movements     # 入出庫レポート
GET    /api/reports/abc-analysis  # ABC分析レポート
GET    /api/reports/turnover      # 在庫回転率レポート
```

### 起動方法
```bash
# バックエンド起動
cd backend
./gradlew bootRun

# フロントエンド起動
cd frontend
npm install
npm start

# Docker環境起動
docker-compose up -d
```

## プロジェクト構造
```
cooola/
├── backend/                 # Spring Boot バックエンド
├── frontend/               # AngularJS フロントエンド
├── docker-compose.yml      # 開発環境用Docker設定
├── aws/                    # AWS設定ファイル
└── docs/                   # ドキュメント
```

## ライセンス
MIT License "# cooola" 
