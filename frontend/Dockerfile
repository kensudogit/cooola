# COOOLa クラウド型倉庫管理システム - AngularJS フロントエンド用 Dockerfile
# Node.js 18 + AngularJS 1.8.x を使用したWebアプリケーションのコンテナ化設定

# ベースイメージ: Node.js 18 (Alpine版)
# 軽量でセキュリティアップデートが適用された公式イメージ
FROM node:18-alpine

# 作業ディレクトリを設定
# コンテナ内でのアプリケーションの作業ディレクトリ
WORKDIR /app

# package.jsonとpackage-lock.jsonをコピー
# 依存関係の定義ファイルをコンテナに配置
# ビルド時間短縮のため、依存関係を事前にインストール
COPY package*.json ./

# 依存関係をインストール
# npmパッケージをインストール
RUN npm install

# ソースコードをコピー
# アプリケーションのソースコードをコンテナに配置
COPY . .

# ポート3000を公開
# フロントエンドアプリケーションのポート
EXPOSE 3000

# 開発サーバーを起動
# npm startコマンドでAngularJS開発サーバーを起動
CMD ["npm", "start"] 