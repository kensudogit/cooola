# COOOLa クイックスタートガイド

## 🚀 簡単起動

### 方法1: 改善版スクリプト（推奨）
```bash
start-improved.bat
```

### 方法2: 基本スクリプト
```bash
start.bat
```

### 方法3: 手動起動
```bash
docker-compose up -d
```

## 🛠️ 管理スクリプト

### サービス停止
```bash
stop.bat
```

### ログ確認
```bash
logs.bat
```

## 🌐 アクセスURL

| サービス | URL | 説明 |
|---------|-----|------|
| フロントエンド | http://localhost:3000 | メインのWebアプリケーション |
| バックエンドAPI | http://localhost:8080/api | RESTful APIエンドポイント |
| phpMyAdmin | http://localhost:8081 | データベース管理ツール |
| Grafana | http://localhost:3001 | 監視ダッシュボード (admin/admin) |
| Kibana | http://localhost:5601 | ログ分析ツール |
| Prometheus | http://localhost:9090 | メトリクス収集・監視 |

## 📋 よくあるコマンド

### サービス状態確認
```bash
docker-compose ps
```

### 全ログ表示
```bash
docker-compose logs
```

### リアルタイムログ
```bash
docker-compose logs -f
```

### 特定サービスのログ
```bash
docker-compose logs backend
docker-compose logs frontend
docker-compose logs mysql
```

### サービス停止
```bash
docker-compose down
```

### 完全クリーンアップ（データも削除）
```bash
docker-compose down -v
```

## ⚠️ トラブルシューティング

### Dockerが起動していない
- Docker Desktopを起動してください
- システムトレイでDockerアイコンを確認

### ポートが使用中
- 他のアプリケーションが同じポートを使用していないか確認
- 必要に応じてポートを変更

### ビルドエラー
- `docker-compose logs`でエラー詳細を確認
- システムリソース（メモリ、ディスク）を確認

### データベース接続エラー
- MySQLコンテナが正常に起動しているか確認
- 環境変数が正しく設定されているか確認

## 📞 サポート

問題が解決しない場合は：
1. `TROUBLESHOOTING.md`を参照
2. `docker-compose logs`でエラーログを確認
3. システム要件を確認

## 🔧 システム要件

- Windows 10/11
- Docker Desktop 4.0以上
- 最低4GB RAM
- 10GB以上の空きディスク容量 