@echo off
chcp 65001 > nul
echo ========================================
echo COOOLa クラウド型倉庫管理システム - ログ表示
echo ========================================
echo.

echo ログを表示するサービスを選択してください:
echo.
echo 1. 全サービスのログ
echo 2. バックエンドのログ
echo 3. フロントエンドのログ
echo 4. MySQLのログ
echo 5. リアルタイムログ（全サービス）
echo 6. 終了
echo.

set /p choice="選択してください (1-6): "

if "%choice%"=="1" (
    echo 全サービスのログを表示中...
    docker-compose logs
    pause
    goto :eof
)

if "%choice%"=="2" (
    echo バックエンドのログを表示中...
    docker-compose logs backend
    pause
    goto :eof
)

if "%choice%"=="3" (
    echo フロントエンドのログを表示中...
    docker-compose logs frontend
    pause
    goto :eof
)

if "%choice%"=="4" (
    echo MySQLのログを表示中...
    docker-compose logs mysql
    pause
    goto :eof
)

if "%choice%"=="5" (
    echo リアルタイムログを表示中...（Ctrl+Cで終了）
    docker-compose logs -f
    goto :eof
)

if "%choice%"=="6" (
    echo 終了します。
    goto :eof
)

echo 無効な選択です。
pause 