@echo off
chcp 65001 > nul
echo ========================================
echo COOOLa クラウド型倉庫管理システム - 状態確認
echo ========================================
echo.

echo 1. コンテナの状態を確認中...
docker-compose ps

echo.
echo 2. 各サービスのログを確認中...
echo.

echo === バックエンドのログ ===
docker-compose logs --tail=5 backend

echo.
echo === フロントエンドのログ ===
docker-compose logs --tail=5 frontend

echo.
echo === MySQLのログ ===
docker-compose logs --tail=5 mysql

echo.
echo ========================================
echo サービスアクセス情報:
echo ========================================
echo フロントエンド: http://localhost:3002
echo バックエンドAPI: http://localhost:8082/api
echo phpMyAdmin: http://localhost:8081
echo Grafana: http://localhost:3001 (admin/admin)
echo Kibana: http://localhost:5601
echo Prometheus: http://localhost:9090
echo ========================================
echo.

echo 3. ネットワーク接続をテスト中...
echo.

echo バックエンドAPI接続テスト:
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:8082/api/actuator/health || echo "バックエンドに接続できません"

echo.
echo フロントエンド接続テスト:
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:3002 || echo "フロントエンドに接続できません"

echo.
echo ========================================
echo 状態確認が完了しました！
echo ========================================
echo.

pause 