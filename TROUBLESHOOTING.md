# COOOLa トラブルシューティングガイド

## よくある問題と解決方法

### 1. 起動スクリプトの文字化けエラー

**問題**: `start.bat`実行時に文字化けエラーが発生する

**解決方法**:
- `start-simple.bat`を使用してください
- または、コマンドプロンプトで直接以下を実行：
```bash
docker-compose up -d
```

### 2. Dockerfileのビルドエラー

**問題**: `gradle`ディレクトリが見つからないエラー

**解決方法**:
1. `backend`ディレクトリに移動
2. Gradle Wrapperを再生成：
```bash
gradle wrapper
```

### 3. Docker Composeの警告

**問題**: `version`属性が非推奨という警告

**解決方法**:
- 警告は無視して問題ありません
- 最新のDocker Composeでは`version`属性は不要です

### 4. サービスが起動しない場合

**確認手順**:
1. Docker Desktopが起動しているか確認
2. ポートが使用されていないか確認
3. ログを確認：
```bash
docker-compose logs
```

### 5. データベース接続エラー

**解決方法**:
1. MySQLコンテナが起動しているか確認
2. 環境変数が正しく設定されているか確認
3. ネットワークが正しく設定されているか確認

## 手動起動手順

### 1. 基本的な起動
```bash
# プロジェクトディレクトリに移動
cd devlop/cooola

# Docker Composeで起動
docker-compose up -d

# 状態確認
docker-compose ps
```

### 2. 個別サービスの起動
```bash
# データベースのみ起動
docker-compose up -d mysql

# バックエンドのみ起動
docker-compose up -d backend

# フロントエンドのみ起動
docker-compose up -d frontend
```

### 3. ログの確認
```bash
# 全サービスのログ
docker-compose logs

# 特定サービスのログ
docker-compose logs backend
docker-compose logs frontend
docker-compose logs mysql
```

### 4. サービスの停止
```bash
# 全サービス停止
docker-compose down

# ボリュームも削除
docker-compose down -v
```

## アクセスURL

- **フロントエンド**: http://localhost:3000
- **バックエンドAPI**: http://localhost:8080/api
- **phpMyAdmin**: http://localhost:8081
- **Grafana**: http://localhost:3001 (admin/admin)
- **Kibana**: http://localhost:5601
- **Prometheus**: http://localhost:9090

## サポート

問題が解決しない場合は、以下を確認してください：
1. Docker Desktopのバージョン
2. Docker Composeのバージョン
3. システムのリソース使用状況
4. ファイアウォールの設定 