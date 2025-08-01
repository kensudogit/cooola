#!/bin/bash
# ========================================
# COOOLa クラウド型倉庫管理システム - Linux/Mac起動スクリプト
# このスクリプトは開発環境の全サービスを起動します
# ========================================

echo "========================================"
echo "COOOLa クラウド型倉庫管理システム"
echo "========================================"
echo

echo "開発環境を起動しています..."
echo

# Docker Composeでサービスを起動
# -d オプションでバックグラウンド実行
echo "1. Docker Composeでサービスを起動中..."
docker-compose up -d

echo
echo "2. サービス起動状況を確認中..."
# 10秒待機してサービスが完全に起動するのを待つ
sleep 10

echo
echo "3. 各サービスの状態:"
# 起動したサービスの状態を表示
docker-compose ps

echo
echo "========================================"
echo "サービスアクセス情報:"
echo "========================================"
echo "フロントエンド: http://localhost:3000          # メインのWebアプリケーション"
echo "バックエンドAPI: http://localhost:8080/api     # RESTful APIエンドポイント"
echo "phpMyAdmin: http://localhost:8081              # データベース管理ツール"
echo "Grafana: http://localhost:3001 (admin/admin)   # 監視ダッシュボード"
echo "Kibana: http://localhost:5601                  # ログ分析ツール"
echo "Prometheus: http://localhost:9090              # メトリクス収集・監視"
echo "========================================"
echo

# 便利なコマンドの説明
echo "ログを表示するには: docker-compose logs -f    # リアルタイムログ表示"
echo "サービスを停止するには: docker-compose down   # 全サービス停止"
echo

# スクリプト終了前に一時停止（ユーザーが情報を確認できるように）
read -p "Enterキーを押して終了..." 