@echo off
chcp 65001 > nul
echo ========================================
echo COOOLa クラウド型倉庫管理システム - 停止
echo ========================================
echo.

echo サービスを停止しています...
echo.

echo 1. 全サービスを停止中...
docker-compose down
if %errorlevel% neq 0 (
    echo 警告: サービスの停止中にエラーが発生しました。
    echo 手動で確認してください: docker-compose ps
) else (
    echo サービスが正常に停止されました。
)

echo.
echo 2. 現在のコンテナ状態:
docker-compose ps

echo.
echo 停止が完了しました！
echo 完全にクリーンアップする場合は以下を実行してください:
echo docker-compose down -v
echo.
pause 