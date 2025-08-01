@echo off
chcp 65001 > nul
echo ========================================
echo COOOLa クラウド型倉庫管理システム - クリーンリビルド
echo ========================================
echo.

echo 警告: この操作により全てのコンテナとイメージが削除されます。
echo データベースのデータも失われます。
echo.
set /p confirm="続行しますか？ (y/N): "

if /i not "%confirm%"=="y" (
    echo 操作をキャンセルしました。
    pause
    exit /b 0
)

echo.
echo 1. 既存のコンテナとイメージを削除中...
docker-compose down -v --rmi all
if %errorlevel% neq 0 (
    echo 警告: 一部のリソースの削除に失敗しました。
)

echo.
echo 2. Dockerキャッシュをクリア中...
docker system prune -f
if %errorlevel% neq 0 (
    echo 警告: キャッシュのクリアに失敗しました。
)

echo.
echo 3. イメージを再ビルド中...
docker-compose build --no-cache
if %errorlevel% neq 0 (
    echo エラー: イメージのビルドに失敗しました。
    pause
    exit /b 1
)

echo.
echo 4. サービスを起動中...
docker-compose up -d
if %errorlevel% neq 0 (
    echo エラー: サービスの起動に失敗しました。
    pause
    exit /b 1
)

echo.
echo 5. サービス起動状況を確認中...
timeout /t 15 /nobreak > nul
docker-compose ps

echo.
echo ========================================
echo クリーンリビルドが完了しました！
echo ========================================
echo.
echo アクセスURL:
echo フロントエンド: http://localhost:3002
echo バックエンドAPI: http://localhost:8082/api
echo phpMyAdmin: http://localhost:8081
echo ========================================
echo.

pause 