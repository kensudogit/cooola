@echo off
chcp 65001 > nul
echo ========================================
echo COOOLa クラウド型倉庫管理システム
echo ========================================
echo.

echo 開発環境を起動しています...
echo.

REM Dockerが起動しているかチェック
echo Dockerの状態を確認中...
docker version > nul 2>&1
if %errorlevel% neq 0 (
    echo エラー: Dockerが起動していません。
    echo Docker Desktopを起動してから再実行してください。
    pause
    exit /b 1
)

REM Docker Composeが利用可能かチェック
echo Docker Composeの状態を確認中...
docker-compose version > nul 2>&1
if %errorlevel% neq 0 (
    echo エラー: Docker Composeが利用できません。
    echo Docker Desktopの設定を確認してください。
    pause
    exit /b 1
)

echo 1. Docker Composeでサービスを起動中...
docker-compose up -d
if %errorlevel% neq 0 (
    echo エラー: サービスの起動に失敗しました。
    echo ログを確認してください: docker-compose logs
    pause
    exit /b 1
)

echo.
echo 2. サービス起動状況を確認中...
echo サービスが完全に起動するまで待機中...
timeout /t 15 /nobreak > nul

echo.
echo 3. 各サービスの状態:
docker-compose ps

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

echo 便利なコマンド:
echo - ログを表示: docker-compose logs -f
echo - サービス停止: docker-compose down
echo - 特定サービスのログ: docker-compose logs [サービス名]
echo.

echo 起動が完了しました！
pause 