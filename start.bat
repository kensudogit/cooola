@echo off
echo ========================================
echo COOOLa クラウド型倉庫管理システム
echo ========================================
echo.

echo 開発環境を起動しています...
echo.

REM Docker Composeでサービスを起動
echo 1. Docker Composeでサービスを起動中...
docker-compose up -d

echo.
echo 2. サービス起動状況を確認中...
timeout /t 10 /nobreak > nul

echo.
echo 3. 各サービスの状態:
docker-compose ps

echo.
echo ========================================
echo サービスアクセス情報:
echo ========================================
echo フロントエンド: http://localhost:3000
echo バックエンドAPI: http://localhost:8080/api
echo phpMyAdmin: http://localhost:8081
echo Grafana: http://localhost:3001 (admin/admin)
echo Kibana: http://localhost:5601
echo Prometheus: http://localhost:9090
echo ========================================
echo.

echo ログを表示するには: docker-compose logs -f
echo サービスを停止するには: docker-compose down
echo.

pause 